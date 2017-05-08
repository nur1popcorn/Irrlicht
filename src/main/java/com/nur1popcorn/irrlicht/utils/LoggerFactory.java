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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

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
    private static SocketHandler socketHandler;

    static
    {
        try
        {
            socketHandler = new SocketHandler("localhost", LogOutput.SERVER_PORT);
            socketHandler.setFormatter(new SocketFormatter());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

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
        logger.addHandler(socketHandler);
        logger.setLevel(Level.ALL);
        LOGGER_MAP.put(clazz, logger);
        return logger;
    }

    /**
     * This class is used to format the record before being sent to the logging server.
     */
    public static class SocketFormatter extends Formatter
    {
        @Override
        public String format(LogRecord record)
        {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try
            {
                record.getSourceMethodName(); //obtain caller class before serialization.
                final ObjectOutput objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(record);
                objectOutputStream.flush();
                return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()) + "\n";
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    byteArrayOutputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
