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

import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;

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
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.CONSTRUCTOR)
    public void construct(float ticksPerSecond);
    
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public float getTicksPerSecond();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getLastHRTime();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public int getElapsedTicks();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public float getRenderPartialTicks();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public float getTimerSpeed();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public float getElapsedPartialTicks();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public void setTicksPerSecond(float ticksPerSecond);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setLastHRTime(double lastHRTime);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setElapsedTicks(int elapsedTicks);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setRenderPartialTicks(float renderPartialTicks);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setTimerSpeed(float timerSpeed);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public void setElapsedPartialTicks(float elapsedPartialTicks);
}
