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

package com.nur1popcorn.irrlicht.engine.wrappers.client.entity;

import com.nur1popcorn.irrlicht.engine.hooker.Hooker;
import com.nur1popcorn.irrlicht.engine.hooker.HookingMethod;
import com.nur1popcorn.irrlicht.engine.hooker.impl.UpdateEvent;
import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;
import com.nur1popcorn.irrlicht.engine.wrappers.client.network.NetHandler;
import org.objectweb.asm.Opcodes;

/**
 * The {@link PlayerSp} is the player, one is controlling.
 *
 * @see ClientPlayer
 * @see Minecraft
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.STRING_CONST,
                 declaring = Minecraft.class,
                 constants = { "minecraft:container", "minecraft:chest", "minecraft:hopper" })
public interface PlayerSp extends ClientPlayer
{
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public NetHandler getNetHandler();

    @HookingMethod(value = UpdateEvent.class,
                   flags = Hooker.DEFAULT | Hooker.OPCODES | Hooker.AFTER,
                   opcodes = {
                       //inject after super call.
                       Opcodes.ALOAD,
                       Opcodes.INVOKESPECIAL
                   })
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.OPCODES,
                     opcodes = {
                          Opcodes.ALOAD,
                          Opcodes.GETFIELD,
                          Opcodes.INVOKESPECIAL,
                          Opcodes.INVOKEVIRTUAL,
                          Opcodes.IFNE
                     })
    public void onUpdate();
}
