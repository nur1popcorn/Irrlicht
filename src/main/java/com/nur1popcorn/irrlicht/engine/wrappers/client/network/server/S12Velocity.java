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

package com.nur1popcorn.irrlicht.engine.wrappers.client.network.server;

import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.network.Packet;

/**
 * The {@link S12Velocity} is a packet is sent to the client to move the player.
 *
 * @see Packet
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
public interface S12Velocity extends Packet
{
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public int getEntityID();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public int getMotionX();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public int getMotionY();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public int getMotionZ();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public void setEntityID(int entityID);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setMotionX(int motionX);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setMotionY(int motionY);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public void setMotionZ(int motionZ);
}
