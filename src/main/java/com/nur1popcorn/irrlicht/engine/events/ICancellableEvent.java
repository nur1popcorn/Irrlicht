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

package com.nur1popcorn.irrlicht.engine.events;

import com.nur1popcorn.irrlicht.engine.events.impl.CancellableEvent;

/**
 * The {@link CancellableEvent} is a {@link Event} which can be cancelled typically resulting
 * in the rest of the caller method being ignored.
 *
 * @see Event
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
public interface ICancellableEvent extends Event
{
    /**
     * @return whether or not the {@link Event} was cancelled.
     */
    public boolean isCancelled();

    /**
     * Sets whether or not the {@link Event} is cancelled.
     *
     * @param cancelled whether or not the {@link Event} is cancelled.
     */
    public void setCancelled(boolean cancelled);
}
