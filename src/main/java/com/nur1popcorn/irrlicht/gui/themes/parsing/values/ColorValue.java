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

package com.nur1popcorn.irrlicht.gui.themes.parsing.values;

import com.nur1popcorn.irrlicht.gui.themes.parsing.exceptions.ParserException;

/**
 * The {@link ColorValue} is a {@link Value} storing a color.
 *
 * @see Value
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class ColorValue extends Value<Integer>
{
    @Override
    public Integer deserialize(String data) throws ParserException
    {
        try
        {
            return (int)Long.parseLong(data.substring(1, 9), 16);
        }
        catch(NumberFormatException e)
        {
            throw new ParserException("Invalid color value: " + data);
        }
    }

    @Override
    public String serialize()
    {
        return "#" + Integer.toHexString(value);
    }
}
