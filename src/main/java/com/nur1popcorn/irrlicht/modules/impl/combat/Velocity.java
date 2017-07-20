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

package com.nur1popcorn.irrlicht.modules.impl.combat;

import com.nur1popcorn.irrlicht.engine.events.EventTarget;
import com.nur1popcorn.irrlicht.engine.hooker.impl.PacketReceiveEvent;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.network.server.S12Velocity;
import com.nur1popcorn.irrlicht.management.values.SliderValue;
import com.nur1popcorn.irrlicht.management.values.ToggleValue;
import com.nur1popcorn.irrlicht.management.values.ValueTarget;
import com.nur1popcorn.irrlicht.modules.Category;
import com.nur1popcorn.irrlicht.modules.Module;
import com.nur1popcorn.irrlicht.modules.ModuleInfo;

/**
 * The {@link Velocity} is a cheat that makes you take less, no or more knock back.
 *
 * @see Module
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@ModuleInfo(name = "Velocity",
            category = Category.COMBAT)
public class Velocity extends Module
{
    @ValueTarget
    private SliderValue<Integer> vertical = new SliderValue<>(this, "Vertical", 0, 0, 200, 1);

    @ValueTarget
    private SliderValue<Integer> horizontal = new SliderValue<>(this, "Horizontal", 0, 0, 200, 1);

    @ValueTarget
    private ToggleValue negative = new ToggleValue(this, "Negative", false);

    @EventTarget
    public void onPacketReceive(PacketReceiveEvent event)
    {
        final Mapper mapper = Mapper.getInstance();
        if(mapper.getMappedClass(S12Velocity.class).isInstance(event.packet.getHandle()))
        {
            final S12Velocity velocityPacket = (S12Velocity)event.packet;
            if(negative.value)
            {
                velocityPacket.setMotionX(velocityPacket.getMotionX() * -1);
                velocityPacket.setMotionZ(velocityPacket.getMotionZ() * -1);
            }
            else
            {
                velocityPacket.setMotionX(velocityPacket.getMotionX() * (horizontal.value / 100));
                velocityPacket.setMotionZ(velocityPacket.getMotionZ() * (horizontal.value / 100));
                velocityPacket.setMotionY(velocityPacket.getMotionY() * (vertical.value / 100));
            }
        }
    }
}