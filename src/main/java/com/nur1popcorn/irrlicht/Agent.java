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

import java.lang.instrument.Instrumentation;

/**
 * The {@link Agent} is the programs entry point.
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
    public static void agentmain(String args, Instrumentation instrumentation)
    {
        try {
            Mapper.getInstance()
                  .generate();
            Hooker.getInstance()
                  .hook(instrumentation);
            Irrlicht.bootstrap();
        } catch (MappingException e) {
            e.printStackTrace();
        }
    }
}
