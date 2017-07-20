/*
 * Copyright (C) Keanu Poeschko - All Rights Reserved
 * Unauthorized copying of this file is strictly prohibited
 *
 * Created by Keanu Poeschko <nur1popcorn@gmail.com>, April 2017
 * This file is part of {Irrlicht}.
 *
 * Do not copy or distribute files of {Irrlicht} without permission of {Keanu Poeschko}
 *
 * Permission to use, copy, modify, and distribute my software for
 * educational, and research purposes, without a signed licensing agreement
 * and for free, is hereby granted, provided that the above copyright notice
 * and this paragraph appear in all copies, modifications, and distributions.
 *
 *
 *
 *
 */

package com.nur1popcorn.irrlicht.launcher.rmi.impl;

import com.nur1popcorn.irrlicht.launcher.rmi.IRmiManager;
import javafx.application.Platform;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@link RmiManager} is used to simplify the task of un-exporting all rmi objects
 * on client shutdown.
 *
 * @see IRmiManager
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
public class RmiManager implements IRmiManager
{
    public static final String IDENTIFIER = "RmiManager";
    public static final int REGISTRY_PORT = 23699;

    private static final Map<String, Remote> REMOTE_MAP = new HashMap<>();

    private static RmiManager instance;

    private List<Runnable> runnables = new ArrayList<>();
    private boolean started;

    //prevent construction :/
    private RmiManager()
    {}

    /**
     * @return a instance of the {@link RmiManager}.
     */
    public static RmiManager getInstance()
    {
        if(instance != null)
            return instance;
        return instance = new RmiManager();
    }

    /**
     * @return a instance of any rmi object registered.
     */
    public static Remote getRMI(String name)
    {
        try
        {
            return LocateRegistry.getRegistry(REGISTRY_PORT)
                                 .lookup(name);
        }
        catch (RemoteException | NotBoundException e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Registers a {@link Remote} for communication with the client.
     *
     * @param name the name under which the remote can be obtained.
     * @param remote the remote which should be obtained.
     */
    public void register(String name, Remote remote)
    {
        REMOTE_MAP.put(name, remote);
    }

    /**
     * Creates a registry and enables all of the {@link Remote}s.
     */
    public void createRegistry() throws RemoteException, AlreadyBoundException
    {
        started = true;
        final Registry registry = LocateRegistry.createRegistry(REGISTRY_PORT);
        REMOTE_MAP.put(IDENTIFIER, this);
        for(Map.Entry<String, Remote> entry : REMOTE_MAP.entrySet())
            registry.bind(entry.getKey(), UnicastRemoteObject.exportObject(entry.getValue(), 0));
    }

    /**
     * @return whether or not the {@link RmiManager} was started.
     *
     * @see #createRegistry()
     * @see #shutdown()
     */
    public boolean wasStarted()
    {
        return started;
    }

    /**
     * Adds a {@link RmiManager} shutdown listener.
     *
     * @param runnable the listener which should be added.
     */
    public void addShutdownListener(Runnable runnable)
    {
        runnables.add(runnable);
    }

    @Override
    public void shutdown()
    {
        if(!started)
            return;
        try
        {
            final Registry registry = LocateRegistry.getRegistry(REGISTRY_PORT);
            for(Map.Entry<String, Remote> entry : REMOTE_MAP.entrySet())
            {
                UnicastRemoteObject.unexportObject(entry.getValue(), true);
                registry.unbind(entry.getKey());
            }
            runnables.forEach(Runnable::run);
        }
        catch (RemoteException | NotBoundException e)
        {
            e.printStackTrace();
        }
    }
}
