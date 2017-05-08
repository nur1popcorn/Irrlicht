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
 * The {@link NumberValue} is a {@link Value} storing a {@link Number}.
 *
 * @see Value
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class NumberValue extends Value<Number>
{
    private Class<? extends Number> type;

    @Override
    public Number deserialize(String data) throws ParserException
    {
        if(data.contains("."))
        {
            type = Double.class;
            return Double.parseDouble(data);
        }
        else
        {
            type = Integer.class;
            return Integer.parseInt(data);
        }
    }

    @Override
    public String serialize() throws ParserException
    {
        if(type.isAssignableFrom(Double.class))
            return Double.toString((Double)value);
        else if(type.isAssignableFrom(Integer.class))
            return Integer.toString((Integer)value);
        throw new ParserException("Could not determine type of number: " + value + ", " + type);
    }

    /**
     * Returns the type of number based on the data provided.
     *
     * @return the type of the number based on the data provided.
     */
    public Class getType()
    {
        return type;
    }
}
