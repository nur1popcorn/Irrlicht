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

import java.util.regex.Pattern;

/**
 * The {@link TokenInfo} stores information about the {@link Token} and how to identify it.
 *
 * @see Token
 * @see Tokenizer
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class TokenInfo
{
    private final Pattern regex;
    private final int type;

    public TokenInfo(Pattern regex, int type)
    {
        this.regex = regex;
        this.type = type;
    }

    /**
     * @return the pattern used to find the token.
     */
    public Pattern getRegex()
    {
        return regex;
    }

    /**
     * @return the token type.
     */
    public int getType()
    {
        return type;
    }
}