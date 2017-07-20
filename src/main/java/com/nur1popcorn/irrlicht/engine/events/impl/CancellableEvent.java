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

package com.nur1popcorn.irrlicht.engine.events.impl;

import com.nur1popcorn.irrlicht.engine.events.Event;
import com.nur1popcorn.irrlicht.engine.events.ICancellableEvent;

/**
 * The {@link CancellableEvent} is a {@link Event} which can be cancelled typically resulting
 * in the rest of the caller method being ignored.
 *
 * @see Event
 *
 * @author nur1popcorn
 * @since 1.0.1-alpha
 */
public class CancellableEvent implements ICancellableEvent
{
    private boolean cancelled;

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }
}
