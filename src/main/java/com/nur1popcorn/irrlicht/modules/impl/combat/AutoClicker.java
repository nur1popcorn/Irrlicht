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

import com.darkmagician6.eventapi.EventTarget;
import com.nur1popcorn.irrlicht.Irrlicht;
import com.nur1popcorn.irrlicht.engine.hooker.events.UpdateEvent;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;
import com.nur1popcorn.irrlicht.management.values.SliderValue;
import com.nur1popcorn.irrlicht.management.values.ValueTarget;
import com.nur1popcorn.irrlicht.modules.Category;
import com.nur1popcorn.irrlicht.modules.Module;
import com.nur1popcorn.irrlicht.modules.ModuleInfo;
import com.nur1popcorn.irrlicht.utils.TimeHelper;
import org.lwjgl.input.Mouse;

/**
 * The {@link AutoClicker} clicks for you.
 *
 * @see Module
 * @see UpdateEvent
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@ModuleInfo(name = "AutoClicker",
            category = Category.COMBAT)
public class AutoClicker extends Module
{
    @ValueTarget
    private SliderValue<Integer> cps = new SliderValue<>(this, "Cps", 12, 1, 20, 1);

    private TimeHelper timer = new TimeHelper();

    @EventTarget
    public void onUpdate(UpdateEvent event)
    {
        Minecraft minecraft;
        if(Mouse.isButtonDown(0) &&
           (minecraft = Irrlicht.getMinecraft()).getGuiScreen().getHandle() == null &&
           timer.hasMSPassed(1000 / cps.value))
        {
            minecraft.setLeftClickDelay(0);
            minecraft.clickMouse();
            timer.reset();
        }
    }
}
