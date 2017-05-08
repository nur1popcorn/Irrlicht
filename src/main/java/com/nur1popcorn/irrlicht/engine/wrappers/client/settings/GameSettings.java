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

package com.nur1popcorn.irrlicht.engine.wrappers.client.settings;

import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;

import java.io.File;

/**
 * The {@link GameSettings} class is storing all of the game's settings.
 *
 * @see Wrapper
 * @see Minecraft
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@DiscoveryMethod(declaring = Minecraft.class)
public interface GameSettings extends Wrapper
{
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.CONSTRUCTOR)
    public void construct(Minecraft minecraft, File file);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.CONSTRUCTOR)
    public void construct();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public boolean isSmoothCameraEnabled();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public boolean isDebugCamEnabled();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public float getFovSetting();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public float getGamma();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public float getSaturation();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public int getGuiScale();

}
