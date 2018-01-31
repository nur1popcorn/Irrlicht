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

package com.nur1popcorn.irrlicht.engine.hooker.impl;

import com.nur1popcorn.irrlicht.engine.events.ILocalVariableEvent;

/**
 * The {@link TimerEvent} is called when the client intercepts "tps"
 * variable from the {@link com.nur1popcorn.irrlicht.engine.wrappers.client.minecraft.Timer}
 * constructor.
 *
 * @see com.nur1popcorn.irrlicht.engine.wrappers.client.minecraft.Timer
 *
 * @author Siphedrion
 * @since 1.1.1-alpha
 */
public class TimerEvent implements ILocalVariableEvent
{
    public float timerSpeed;

    @Override
    public void setLocalVariables(Object[] localVariables)
    {
        timerSpeed = (float) localVariables[0];
    }

    @Override
    public Object[] getLocalVariables()
    {
        return new Object[] {
                timerSpeed
        };
    }
}
