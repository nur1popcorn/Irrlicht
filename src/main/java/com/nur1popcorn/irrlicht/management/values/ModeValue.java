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
 * The {@link ModeValue} is a {@link Value} that is used to switch in between states.
 *
 * @see Value
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class ModeValue<T extends Enum> extends Value<T>
{
    public ModeValue(Module module, String name, T value)
    {
        super(module, name, value);
    }

    @Override
    public void load(String data)
    {
        value = (T)value.getDeclaringClass().getEnumConstants()[Integer.valueOf(data)];
    }

    @Override
    public String save()
    {
        return Integer.toString(value.ordinal());
    }
}
