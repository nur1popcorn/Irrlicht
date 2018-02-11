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

package com.nur1popcorn.irrlicht.engine.wrappers.client.renderer;

import com.nur1popcorn.irrlicht.engine.hooker.HookingMethod;
import com.nur1popcorn.irrlicht.engine.hooker.impl.Render3DEvent;
import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;
import org.objectweb.asm.Opcodes;

/**
 * The {@link EntityRenderer} class is used to do all player rendering
 * events.
 *
 * @see Wrapper
 * @see Minecraft
 *
 * @author Siphedrion
 * @since 1.1.1-alpha
 */
@DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.STRING_CONST,
                 declaring = Minecraft.class,
                 constants = { "shaders/post/notch.json" })
public interface EntityRenderer extends Wrapper
{
    @HookingMethod(value = Render3DEvent.class)
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIRST_MATCH | Mapper.OPCODES,
                     opcodes = {
                          Opcodes.ALOAD,
                          Opcodes.GETFIELD,
                          Opcodes.FLOAD,
                          Opcodes.INVOKEVIRTUAL
                     })
    public void renderHand(float partialTicks, int pass);
}
