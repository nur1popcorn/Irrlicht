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

package com.nur1popcorn.irrlicht.gui.themes.parsing.exceptions;

import com.nur1popcorn.irrlicht.gui.themes.parsing.Token;

/**
 * The {@link ParserException} is thrown when an error occurs while tokenizing or parsing
 * the source.
 *
 * @see com.nur1popcorn.irrlicht.gui.themes.parsing.Tokenizer
 * @see com.nur1popcorn.irrlicht.gui.themes.parsing.Style
 * @see Token
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class ParserException extends Exception
{
    private Token token;

    public ParserException(String message)
    {
        super(message);
    }

    public ParserException(String message, Token token)
    {
        this(message);
        this.token = token;
    }
    /**
     * @return the {@link Token]} through which the exception was caused.
     */
    public Token getToken()
    {
        return token;
    }
}
