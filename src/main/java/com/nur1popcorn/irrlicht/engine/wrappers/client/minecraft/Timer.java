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

package com.nur1popcorn.irrlicht.engine.wrappers.client.minecraft;

import com.nur1popcorn.irrlicht.engine.events.Event;
import com.nur1popcorn.irrlicht.engine.hooker.Hooker;
import com.nur1popcorn.irrlicht.engine.hooker.HookingMethod;
import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;
import org.objectweb.asm.Opcodes;

/**
 * The {@link Timer} class is used to handle the game's timings like updates per second
 * by incrementing this way one could speed up the game.
 *
 * @see Wrapper
 * @see Minecraft
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@DiscoveryMethod(declaring = Minecraft.class)
public interface Timer extends Wrapper
{
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.CONSTRUCTOR | Mapper.FIRST_MATCH | Mapper.OPCODES,
                     opcodes = {
                        Opcodes.ALOAD,
                        Opcodes.INVOKESTATIC,
                        Opcodes.PUTFIELD
                     })
    public void construct(float ticksPerSecond);

    @HookingMethod(value = Event.class, flags = Hooker.CUSTOM)
    public void updateTimer();
}
