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
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.util.AxisAlignedBB;

/**
 * The {@link Entity} class is an abstract class all entities extend.
 *
 * @see Wrapper
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@DiscoveryMethod(declaring = EntityLivingBase.class,
                 checks = Mapper.DEFAULT | Mapper.EXTENSION)
public interface Entity extends Wrapper
{
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public double getPrevX();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getPrevY();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getPrevZ();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getX();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getY();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getZ();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getMotionX();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getMotionY();
    
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getMotionZ();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public float getRotationYaw();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public float getRotationPitch();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public float getPrevRotationYaw();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public float getPrevRotationPitch();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public AxisAlignedBB getBB();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public boolean isOnGround();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public boolean isCollidedHorizontally();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public boolean isCollidedVertically();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public void setPrevX(double prevX);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setPrevY(double prevY);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setPrevZ(double prevZ);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setX(double x);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setY(double y);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setZ(double z);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setMotionX(double motionX);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setMotionY(double motionY);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setMotionZ(double motionZ);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setRotationYaw(float rotationYaw);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setRotationPitch(float rotationPitch);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setPrevRotationYaw(float prevRotationYaw);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setPrevRotationPitch(float prevRotationPitch);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setBB(AxisAlignedBB bb);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setOnGround(boolean onGround);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setCollidedHorizontally(boolean collidedHorizontally);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public void setCollidedVertically(boolean collidedVertically);
}
