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
 * The {@link SliderValue} is a slider {@link Value} providing min, max and increment.
 *
 * @see Value
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class SliderValue<T extends Number> extends Value<T>
{
    public final T min,
                   max,
                   increment;

    public SliderValue(Module module, String name, T value, T min, T max, T increment)
    {
        super(module, name, value);
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    @Override
    public void load(String data)
    {
        if(value instanceof Integer)
            value = (T) Integer.valueOf(data);
        else if(value instanceof Double)
            value = (T) Double.valueOf(data);
        else if(value instanceof Float)
            value = (T) Float.valueOf(data);
        else if(value instanceof Short)
            value = (T) Short.valueOf(data);
    }

    @Override
    public String save()
    {
        return value.toString();
    }
}
