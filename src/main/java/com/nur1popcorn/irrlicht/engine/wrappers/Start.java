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

package com.nur1popcorn.irrlicht.engine.wrappers;

import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;

import java.lang.reflect.Modifier;

/**
 * The {@link Start} class is the main class of Minecraft.
 *
 * @see Wrapper
 * @see Mapper
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@DiscoveryMethod(checks = Mapper.CUSTOM)
public interface Start extends Wrapper
{
    /**
     * The main class's location.
     */
    public static final String DEFAULT_LOC = "net.minecraft.client.main.Main";

    @DiscoveryMethod(modifiers = Modifier.PUBLIC | Modifier.STATIC)
    public void main(String[] args);
}
