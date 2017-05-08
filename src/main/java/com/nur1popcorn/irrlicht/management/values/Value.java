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

package com.nur1popcorn.irrlicht.management.values;

import com.nur1popcorn.irrlicht.modules.Module;

/**
 * The {@link Value} class is used to store, load and save different kinds of settings
 * for modules.
 *
 * @see Module
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public abstract class Value<T>
{
    public T value;
    protected Module module;
    protected String name;

    public Value(Module module, String name, T value)
    {
        this.module = module;
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name of the {@link Value}.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the {@link Module} it is attached to.
     */
    public Module getModule()
    {
        return module;
    }

    /**
     * loads the value from the string provided.
     *
     * @see #save()
     *
     * @param data the information from which the {@link Value} should be loaded.
     */
    public abstract void load(String data);

    /**
     * @see #load(String)
     *
     * @return a serialized version of the {@link Value}.
     */
    public abstract String save();
}
