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

package com.nur1popcorn.irrlicht.gui.components;

/**
 * The {@link LockableComponent} is a disabled {@link Component}.
 *
 * @see Observable
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public abstract class LockableComponent extends Component
{
    private boolean locked;

    public LockableComponent()
    {}

    public LockableComponent(int width, int height)
    {
        super(width, height);
    }

    public LockableComponent(int x, int y, int width, int height)
    {
        super(x, y, width, height);
    }

    /**
     * Toggles the lock of {@link Component}.
     */
    public void lock()
    {
        this.locked = !locked;
    }

    /**
     * @return whether or not the {@link Component} is locked.
     */
    public boolean isLocked()
    {
        return locked;
    }
}
