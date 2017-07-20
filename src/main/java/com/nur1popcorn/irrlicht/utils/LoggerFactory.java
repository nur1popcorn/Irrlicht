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

package com.nur1popcorn.irrlicht.utils;

import com.nur1popcorn.irrlicht.launcher.LogOutput;
import com.nur1popcorn.irrlicht.launcher.rmi.ILogOutput;
import com.nur1popcorn.irrlicht.launcher.rmi.impl.RmiManager;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * The {@link LoggerFactory} is used to assemble and store loggers that connect to
 * the {@link LogOutput}.
 *
 * @see LogOutput
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class LoggerFactory
{
    private static final Map<Class, Logger> LOGGER_MAP = new HashMap<>();
    private static final RmiHandler RMI_HANDLER = new RmiHandler();

    //prevent construction :/
    private LoggerFactory()
    {}

    /**
     * @param clazz the class for which the logger is supposed to be generated.
     *
     * @return a logger for the class with all of the needed handlers attached to it.
     */
    public static Logger getLogger(Class clazz)
    {
        if(LOGGER_MAP.containsKey(clazz))
            return LOGGER_MAP.get(clazz);
        final Logger logger = Logger.getLogger(clazz.getName());
        logger.setUseParentHandlers(false);
        logger.addHandler(RMI_HANDLER);
        logger.setLevel(Level.ALL);
        LOGGER_MAP.put(clazz, logger);
        return logger;
    }

    public static class RmiHandler extends StreamHandler
    {
        private static final ILogOutput LOG_OUTPUT = (ILogOutput) RmiManager.getRMI("LogOutput");

        @Override
        public synchronized void publish(LogRecord record)
        {
            try
            {
                record.getSourceMethodName(); //obtain caller class before serialization.
                LOG_OUTPUT.log(record);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    }
}
