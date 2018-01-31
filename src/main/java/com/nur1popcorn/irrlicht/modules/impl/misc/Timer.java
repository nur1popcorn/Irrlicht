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

package com.nur1popcorn.irrlicht.modules.impl.misc;

import com.nur1popcorn.irrlicht.engine.events.EventTarget;
import com.nur1popcorn.irrlicht.engine.hooker.impl.TimerEvent;
import com.nur1popcorn.irrlicht.engine.hooker.impl.UpdateEvent;
import com.nur1popcorn.irrlicht.management.values.SliderValue;
import com.nur1popcorn.irrlicht.management.values.ValueTarget;
import com.nur1popcorn.irrlicht.modules.Category;
import com.nur1popcorn.irrlicht.modules.Module;
import com.nur1popcorn.irrlicht.modules.ModuleInfo;

/**
 * The {@link Timer} is a cheat that accelerates the game's number of updates effectively
 * incrementing the number of packets being sent and the number of movements being made.
 *
 * @see Module
 * @see UpdateEvent
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@ModuleInfo(name = "Timer",
            category = Category.MISC)
public class Timer extends Module
{
    @ValueTarget
    private SliderValue<Float> timerSpeed = new SliderValue<>(this, "Speed", 1f, 0.1f, 10f, 0.1f);

    @EventTarget
    public void onTimerEvent(TimerEvent event)
    {
        System.out.println("test");
        System.out.println(event.timerSpeed);
    }
}
