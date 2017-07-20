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

package com.nur1popcorn.irrlicht.utils;

/**
 * The {@link StringUtils} is an util providing all sorts of useful methods concerning
 * strings.
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
public class StringUtils
{
    //prevent construction :/
    private StringUtils()
    {}

    /**
     * @param s the string which should be capitalized.
     * @param delimiter the character after which the next letter should be capitalized.
     *
     * @return a capitalized version of the string provided.
     */
    public static String capitalize(String s, char delimiter)
    {
        boolean capitalize = true;
        final char chars[] = s.toCharArray();
        for(int i = 0; i < s.length(); i++)
        {
            chars[i] = capitalize ? Character.toUpperCase(chars[i]) : Character.toLowerCase(chars[i]);
            capitalize = chars[i] == delimiter;
        }
        return new String(chars);
    }
}
