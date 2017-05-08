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

package com.nur1popcorn.irrlicht.management.keybind;

import org.lwjgl.input.Keyboard;

/**
 * The {@link KeyMask} is used by {@link KeyBind}s to create more complex combinations like:
 * CTRL+X, CTRL+Z, etc.
 *
 * @see KeyBind
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public enum KeyMask
{
    NONE(null),
    SHIFT(new int[] {
        Keyboard.KEY_LSHIFT,
        Keyboard.KEY_RSHIFT
    }),
    CONTROL(new int[] {
        Keyboard.KEY_LCONTROL,
        Keyboard.KEY_RCONTROL
    }),
    ALT(new int[] {
        Keyboard.KEY_LMENU,
        Keyboard.KEY_RMENU
    });

    private final int[] keys;

    KeyMask(int[] keys)
    {
        this.keys = keys;
    }

    /**
     * @return all keys associated with the mask.
     */
    public int[] getKeys()
    {
        return keys;
    }

    /**
     * @return whether or not the {@link KeyMask} is down.
     */
    public boolean isDown()
    {
        if(keys != null)
        {
            for(int key : keys)
                if(Keyboard.isKeyDown(key))
                    return true;
            return false;
        }
        return true;
    }
}
