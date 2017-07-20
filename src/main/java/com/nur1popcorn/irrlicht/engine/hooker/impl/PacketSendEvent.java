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

package com.nur1popcorn.irrlicht.engine.hooker.impl;

import com.nur1popcorn.irrlicht.engine.events.ILocalVariableEvent;
import com.nur1popcorn.irrlicht.engine.events.impl.CancellableEvent;
import com.nur1popcorn.irrlicht.engine.mapper.WrapperDelegationHandler;
import com.nur1popcorn.irrlicht.engine.wrappers.client.network.Packet;

/**
 * The {@link PacketSendEvent} is called when the client is attempting to send a packet.
 *
 * @see CancellableEvent
 * @see Packet
 * @see WrapperDelegationHandler
 * @see com.nur1popcorn.irrlicht.engine.wrappers.client.network.NetworkManager
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
public class PacketSendEvent extends CancellableEvent implements ILocalVariableEvent
{
    public Packet packet;

    @Override
    public void setLocalVariables(Object[] localVariables)
    {
        packet = WrapperDelegationHandler.createWrapperProxy(Packet.class, localVariables[0]);
    }

    @Override
    public Object[] getLocalVariables()
    {
        return new Object[] {
            packet.getHandle()
        };
    }
}
