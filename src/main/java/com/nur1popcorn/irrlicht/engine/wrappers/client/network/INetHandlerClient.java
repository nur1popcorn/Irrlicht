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

import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;

/**
 * The {@link INetHandlerClient} is used to handle packets sent by the server.
 *
 * @see Wrapper
 * @see NetHandlerClient
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
@DiscoveryMethod(checks = Mapper.CUSTOM)
public interface INetHandlerClient extends Wrapper
{
    //public void handleEntityVelocity(S12Velocity packetIn);
}
