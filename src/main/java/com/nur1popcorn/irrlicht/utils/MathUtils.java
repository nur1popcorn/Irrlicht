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
import java.math.RoundingMode;

/**
 * The {@link MathUtils} is an util providing all sorts of useful methods concerning
 * math.
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
public class MathUtils
{
    //prevent construction :/
    private MathUtils()
    {}

    /**
     * @param number the number which should be rounded.
     * @param places the number of places which should be kept.
     *
     * @return the provided number rounded to the number of places.
     */
    public static double round(double number, int places)
    {
        assert places > 0;
        final double multiplier = Math.pow(10, places);
        return (int)(number * multiplier) / multiplier;
    }

    /**
     * <p>Rounds the number cleanly.</p>
     * <p><i>Note:</i>should be used when the result needs to accurate.</p>
     *
     * @param number the number which should be rounded.
     * @param places the number of places which should be kept.
     *
     * @return the provided number rounded to the number of places.
     */
    public static double roundClean(double number, int places)
    {
        assert places > 0;
        return new BigDecimal(number)
                .setScale(places, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
