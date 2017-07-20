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

package com.nur1popcorn.irrlicht.launcher.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The {@link IRmiManager} is to listen for client shutdown.
 *
 * @see Remote
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
public interface IRmiManager extends Remote
{
    /**
     * Shuts down the launcher's
     * {@link com.nur1popcorn.irrlicht.launcher.rmi.impl.RmiManager}.
     *
     * @throws RuntimeException
     */
    public void shutdown() throws RemoteException;
}
