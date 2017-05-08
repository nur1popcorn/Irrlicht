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
 * The {@link KeyBind} class is used to store and check key binds easily.
 *
 * @see KeyMask
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class KeyBind
{
    private KeyMask keyMask;
    private int key;

    public KeyBind(KeyMask keyMask, int key)
    {
        this.keyMask = keyMask;
        this.key = key;
    }

    /**
     * @return the {@link KeyBind}'s {@link KeyMask}
     */
    public KeyMask getKeyMask()
    {
        return keyMask;
    }

    /**
     * @return the {@link KeyBind}'s key.
     */
    public int getKey()
    {
        return key;
    }

    /**
     * @return whether or not the {@link KeyBind} is down.
     */
    public boolean isDown()
    {
        return keyMask.isDown() && Keyboard.isKeyDown(key);
    }

    /**
     * @return whether or not the {@link KeyBind} is pressed.
     */
    public boolean isPressed()
    {
        //TODO: implement.
        throw new RuntimeException();
    }
}
