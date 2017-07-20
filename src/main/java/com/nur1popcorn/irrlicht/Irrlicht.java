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

package com.nur1popcorn.irrlicht;

import com.nur1popcorn.irrlicht.engine.events.EventManager;
import com.nur1popcorn.irrlicht.engine.events.EventTarget;
import com.nur1popcorn.irrlicht.engine.hooker.impl.SwapBuffersEvent;
import com.nur1popcorn.irrlicht.engine.mapper.WrapperDelegationHandler;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;
import com.nur1popcorn.irrlicht.gui.GuiManager;
import com.nur1popcorn.irrlicht.launcher.rmi.ILauncher;
import com.nur1popcorn.irrlicht.launcher.rmi.IPerformanceCharts;
import com.nur1popcorn.irrlicht.launcher.rmi.IRmiManager;
import com.nur1popcorn.irrlicht.launcher.rmi.impl.RmiManager;
import com.nur1popcorn.irrlicht.modules.ModuleManager;
import com.nur1popcorn.irrlicht.management.GameConfig;
import com.nur1popcorn.irrlicht.utils.LoggerFactory;
import com.nur1popcorn.irrlicht.utils.MathUtils;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@link Irrlicht} is the main class of the client.
 *
 * @see ModuleManager
 * @see GuiManager
 * @see ILauncher
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Irrlicht
{
    /**
     * The name of the client.
     */
    public static final String NAME = "Irrlicht",
    /**
     * The client's version.
     */
                               VERSION = "1.1.0-alpha";

    /**
     * An array containing all of the authors names.
     */
    public static final String AUTHORS[] =
    {
        "nur1popcorn"
    };

    private static Irrlicht instance;

    private final Logger logger = LoggerFactory.getLogger(Irrlicht.class);
    private final GameConfig gameConfig;
    private final ModuleManager moduleManager;
    private final GuiManager guiManager;

    private int frameCounter;

    //prevent construction :/
    private Irrlicht(GameConfig gameConfig)
    {
        instance = this;
        this.gameConfig = gameConfig;
        logger.log(Level.INFO, "Detected version: " + gameConfig.version);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try
            {
                ((IRmiManager) RmiManager.getRMI(RmiManager.IDENTIFIER)).shutdown();
                logger.log(Level.INFO, "Shutting down...");
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }, NAME + " - ShutdownHook"));

        EventManager.register(this);

        final Thread performanceThread = new Thread(() -> {
            try
            {
                for(;;)
                {
                    final IPerformanceCharts performanceCharts = (IPerformanceCharts) RmiManager.getRMI("PerformanceCharts");

                    final Runtime runtime = Runtime.getRuntime();
                    final int usedMemory = (int)((runtime.totalMemory() - runtime.freeMemory()) / 1000_000);
                    performanceCharts.update(IPerformanceCharts.Type.RAM, usedMemory + "MB /" + (runtime.maxMemory() / 1000_000) + "MB", usedMemory);

                    final OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
                    performanceCharts.update(IPerformanceCharts.Type.CPU_LOAD, MathUtils.roundClean(operatingSystemMXBean.getProcessCpuLoad() * 100, 2) + "%", (int)(operatingSystemMXBean.getProcessCpuLoad() * 10000));

                    final int fps = frameCounter * (1000 / 75);
                    performanceCharts.update(IPerformanceCharts.Type.FPS, fps + "FPS", fps);
                    frameCounter = 0;
                    Thread.sleep(75);
                }
            }
            catch (InterruptedException | RemoteException e)
            {
                e.printStackTrace();
            }
        }, NAME + " - PerformanceThread");
        performanceThread.setDaemon(true);
        performanceThread.start();

        moduleManager = ModuleManager.createModuleManager();
        guiManager = GuiManager.createGuiManager();
    }

    @EventTarget
    public void onSwapBuffers(SwapBuffersEvent event)
    {
        frameCounter++;
    }

    /**
     * Creates an instance of the client.
     */
    public static void bootstrap(GameConfig gameConfig)
    {
        new Irrlicht(gameConfig);
    }

    /**
     * @return an instance of the {@link Irrlicht} class;
     */
    public static Irrlicht getIrrlicht()
    {
        return instance;
    }

    /**
     * @see WrapperDelegationHandler
     * @see Minecraft
     *
     * @return an instance of the {@link Minecraft} class.
     */
    public static Minecraft getMinecraft()
    {
        return WrapperDelegationHandler.createWrapperProxy(Minecraft.class, null)
                                       .getMinecraft();
    }

    /**
     * @see ModuleManager
     *
     * @return an instance of the {@link ModuleManager}
     */
    public ModuleManager getModuleManager()
    {
        return moduleManager;
    }

    /**
     * @see GuiManager
     *
     * @return an instance of the {@link GuiManager}
     */
    public GuiManager getGuiManager()
    {
        return guiManager;
    }
}
