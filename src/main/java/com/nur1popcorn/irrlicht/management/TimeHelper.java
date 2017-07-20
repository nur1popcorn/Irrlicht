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

package com.nur1popcorn.irrlicht.management;

/**
 * The {@link TimeHelper} is an util used to make it easier measuring the time passed.
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class TimeHelper
{
    private long startTime;

    public TimeHelper()
    {
        reset();
    }

    /**
     * Resets the amount of time passed.
     */
    public void reset()
    {
        startTime = System.currentTimeMillis();
    }

    /**
     * @return the amount of ms passed.
     */
    public long getMSPassed()
    {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * @param ms the amount of time which should have passed.
     *
     * @return whether or not the amount of ms have passed.
     */
    public boolean hasMSPassed(long ms)
    {
        return getMSPassed() >= ms;
    }
}
