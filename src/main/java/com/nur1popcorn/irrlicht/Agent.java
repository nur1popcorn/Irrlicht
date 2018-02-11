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

import com.nur1popcorn.irrlicht.engine.exceptions.MappingException;
import com.nur1popcorn.irrlicht.engine.hooker.Hooker;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.management.GameConfig;
import com.nur1popcorn.irrlicht.utils.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@link Agent} is the agent's entry point.
 *
 * @see Mapper
 * @see Hooker
 * @see Irrlicht
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Agent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Agent.class);

    public static void premain(String args, Instrumentation instrumentation)
    {
        agentmain(args, instrumentation);
    }

    public static void agentmain(String args, Instrumentation instrumentation)
    {
        final Map<String, String[]> agentParameters = new HashMap<>();
        for(String s : args.split(","))
        {
            final String[] values = s.split("=");
            if(values.length >= 2)
                agentParameters.put(values[0], Arrays.copyOfRange(values, 1, values.length));
        }

        try
        {
            Irrlicht.bootstrap(
                new GameConfig(
                    agentParameters.get("version")[0],
                    agentParameters.get("gameDir")[0],
                    agentParameters.get("assetsDir")[0],
                    agentParameters.get("main")[0]
                )
            );
            Mapper.getInstance()
                  .generate();
            Hooker.createHooker()
                  .hook(instrumentation);
        }
        catch (MappingException e)
        {
            // TODO: Better error handling
            LOGGER.log(Level.SEVERE, "");
            for(Class<? extends Wrapper> wrapper : e.getWrappers())
            {
                LOGGER.log(Level.SEVERE, wrapper.toString());
                for(Method method : wrapper.getDeclaredMethods())
                    if (Mapper.getInstance().getMappedMethod(method) == null)
                        LOGGER.log(Level.SEVERE, method.toString());
            }
            LOGGER.log(Level.SEVERE, "", e);
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.log(Level.SEVERE, "Got an exception.", e);
        }
    }
}
