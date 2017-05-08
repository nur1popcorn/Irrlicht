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

package com.nur1popcorn.irrlicht.gui.themes.parsing;

/**
 * The {@link Token} a token ready to be parsed.
 *
 * @see Tokenizer
 * @see TokenInfo
 * @see Style
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Token
{
    private final int tokenType;
    private String source;

    public Token(int tokenType, String source)
    {
        this.tokenType = tokenType;
        this.source = source;
    }

    /**
     * @return the opcode of the {@link Token}.
     */
    public int getTokenType()
    {
        return tokenType;
    }


    /**
     * Sets the {@link Token}'s description.
     *
     * @param source the {@link Token}'s description.
     */
    public void setSource(String source)
    {
        this.source = source;
    }

    /**
     * @return the description of the {@link Token}.
     */
    public String getSource()
    {
        return source;
    }
}
