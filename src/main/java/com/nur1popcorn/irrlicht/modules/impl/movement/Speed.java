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

package com.nur1popcorn.irrlicht.modules.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.nur1popcorn.irrlicht.engine.hooker.events.UpdateEvent;
import com.nur1popcorn.irrlicht.management.values.SliderValue;
import com.nur1popcorn.irrlicht.management.values.ValueTarget;
import com.nur1popcorn.irrlicht.modules.Category;
import com.nur1popcorn.irrlicht.modules.Module;
import com.nur1popcorn.irrlicht.modules.ModuleInfo;
import com.nur1popcorn.irrlicht.utils.MovementUtils;

/**
 * The {@link Speed} is a cheat that accelerates the player's movement.
 *
 * @see Module
 * @see UpdateEvent
 * @see MovementUtils
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@ModuleInfo(name = "Speed",
            category = Category.MOVEMENT)
public class Speed extends Module
{
    @ValueTarget
    private SliderValue<Double> speed = new SliderValue<>(this, "Speed", 1d, 0.1d, 2.5d, 0.1d);

    @EventTarget
    public void onUpdate(UpdateEvent event)
    {
        MovementUtils.setSpeed(speed.value);
    }
}