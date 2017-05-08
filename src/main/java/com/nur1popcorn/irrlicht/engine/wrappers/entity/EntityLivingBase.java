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

package com.nur1popcorn.irrlicht.engine.wrappers.entity;

import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;

/**
 * The {@link Entity} class is an abstract class all living entities extend.
 *
 * @see Entity
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@DiscoveryMethod(declaring = EntityPlayer.class,
                 checks = Mapper.DEFAULT | Mapper.EXTENSION)
public interface EntityLivingBase extends Entity
{
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public boolean isJumping();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public float getStrafe();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public float getForward();
}
