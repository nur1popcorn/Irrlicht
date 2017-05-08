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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The {@link NumberUtils} is an util storing useful methods to do with {@link Number}s.
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class NumberUtils
{
    //prevent construction :/
    private NumberUtils()
    {}

    /**
     * This function can be used to convert any {@link Number} to any other {@link Number}.
     *
     * @param number the {@link Number} that should be converted.
     * @param clazz the class it should be converted to.
     *
     * @return a converted version of the number cast to the class provided.
     */
    public static Number convertToTarget(Number number, Class<? extends Number> clazz)
    {
        if(clazz.isInstance(number))
            return number;
        else if (clazz.equals(Integer.class))
            return number.intValue();
        else if(clazz.equals(Float.class))
            return number.floatValue();
        else if(clazz.equals(Double.class))
            return number.doubleValue();
        else if(clazz.equals(Long.class))
            return number.longValue();
        else if(clazz.equals(Byte.class))
            return number.byteValue();
        else if(clazz.equals(Short.class))
            return number.shortValue();
        else if(clazz.equals(BigInteger.class))
            return BigInteger.valueOf(number.longValue());
        else if(clazz.equals(BigDecimal.class))
            return new BigDecimal(number.toString());
        throw new RuntimeException("Could determine number type: " + clazz.getName());
    }
}
