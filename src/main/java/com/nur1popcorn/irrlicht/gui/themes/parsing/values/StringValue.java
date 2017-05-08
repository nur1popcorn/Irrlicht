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
 * The {@link StringValue} is a {@link Value} storing a string.
 *
 * @see Value
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class StringValue extends Value<String>
{
    @Override
    public String deserialize(String data) throws ParserException
    {
        return data.substring(1, data.length() - 1);
    }

    @Override
    public String serialize() throws ParserException
    {
        return "'" + value + "'";
    }
}
