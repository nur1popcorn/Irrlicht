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
 * The {@link Value} is used by {@link com.nur1popcorn.irrlicht.gui.themes.parsing.Style}s
 * to easily store, load and save data.
 *
 * @see com.nur1popcorn.irrlicht.gui.themes.parsing.Style
 * @see StringValue
 * @see NumberValue
 * @see ColorValue
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public abstract class Value<T>
{
    public T value;

    //prevent construction :/
    protected Value()
    {}

    /**
     * Factory method for {@link Value}s returns a {@link Value} based on the string provided.
     *
     * @param data the string used to create a {@link Value}.
     *
     * @throws ParserException if the data provided is invalid.
     *
     * @return the {@link Value} based on the data provided.
     */
    public static Value parse(String data) throws ParserException
    {
        final Value value;
        if(data.startsWith("#") && data.length() == 9)
            value = new ColorValue();
        else if(data.startsWith("'") && data.endsWith("'"))
            value = new StringValue();
        else if(data.matches("[0-9.]+?"))
            value = new NumberValue();
        else
            throw new ParserException("Invalid value: " + data);
        value.value = value.deserialize(data);
        return value;
    }

    /**
     * @param data the data provided to be turned into a {@link Value}.
     *
     * @throws ParserException if the data provided is invalid.
     *
     * @see #parse(String)
     *
     * @return a {@link Value} based on the data.
     */
    public abstract T deserialize(String data) throws ParserException;

    /**
     * Turns the {@link Value} into the string which can be read.
     *
     * @throws ParserException if an error occurs serializing the {@link Value}.
     *
     * @see #deserialize(String)
     * @see #parse(String)
     *
     * @return a string based on the {@link Value}.
     */
    public abstract String serialize() throws ParserException;
}
