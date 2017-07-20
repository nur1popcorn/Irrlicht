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

package com.nur1popcorn.irrlicht.engine.wrappers.client.network;

import com.nur1popcorn.irrlicht.engine.hooker.Hooker;
import com.nur1popcorn.irrlicht.engine.hooker.HookingMethod;
import com.nur1popcorn.irrlicht.engine.hooker.impl.PacketReceiveEvent;
import com.nur1popcorn.irrlicht.engine.hooker.impl.PacketSendEvent;
import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import io.netty.channel.ChannelHandlerContext;
import org.objectweb.asm.Opcodes;

/**
 * The {@link NetworkManager} is a class used handle in/out going {@link Packet}s.
 *
 * @see Wrapper
 * @see NetHandlerClient
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
@DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.STRING_CONST,
                 declaring = NetHandlerClient.class,
                 constants = { "decompress",
                               "compress",
                               "decoder" })
public interface NetworkManager extends Wrapper
{
    @HookingMethod(value = PacketReceiveEvent.class,
                   flags = Hooker.DEFAULT | Hooker.OPCODES | Hooker.AFTER,
                   opcodes = {
                       Opcodes.ALOAD,
                       Opcodes.GETFIELD,
                       Opcodes.INVOKEINTERFACE,
                       Opcodes.IFEQ
                   }, indices = {
                      /* indices: 0 -> this
                       *          1 -> channelHandlerContext
                       *          2 -> packet */
                       2
                   }, overwrite = {
                       2
                   })
    public void readChannel0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception;

    @HookingMethod(value = PacketSendEvent.class,
                   flags = Hooker.DEFAULT | Hooker.BEFORE,
                   indices = {
                   /* indices: 0 -> this
                    *          1 -> packet */
                       1
                   }, overwrite = {
                       1
                   })
    public void sendPacket(Packet packet);
}
