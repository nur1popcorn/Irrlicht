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

import com.nur1popcorn.irrlicht.Irrlicht;
import com.nur1popcorn.irrlicht.engine.events.EventTarget;
import com.nur1popcorn.irrlicht.engine.hooker.impl.UpdateEvent;
import com.nur1popcorn.irrlicht.modules.Category;
import com.nur1popcorn.irrlicht.modules.Module;
import com.nur1popcorn.irrlicht.modules.ModuleInfo;

/**
 * The {@link Fly} is a cheat that allows the player to fly.
 *
 * @see Module
 * @see UpdateEvent
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@ModuleInfo(name = "Fly",
            category = Category.MOVEMENT)
public class Fly extends Module
{
    @Override
    public void onDisable()
    {
        super.onDisable();
        Irrlicht.getMinecraft()
                .getPlayer()
                .getPlayerAbilities()
                .setFlying(false);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event)
    {
        Irrlicht.getMinecraft()
                .getPlayer()
                .getPlayerAbilities()
                .setFlying(true);
    }
}
