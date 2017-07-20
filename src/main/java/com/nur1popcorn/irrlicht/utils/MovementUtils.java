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

import com.nur1popcorn.irrlicht.Irrlicht;
import com.nur1popcorn.irrlicht.engine.wrappers.client.entity.PlayerSp;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The {@link MovementUtils} is an util providing all sorts of movement helpers.
 *
 * @see Irrlicht
 * @see PlayerSp
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class MovementUtils
{
    //prevent construction :/
    private MovementUtils()
    {}

    /**
     * @return whether or not the {@link PlayerSp} is moving.
     *
     * @see PlayerSp
     * @see Irrlicht
     */
    public static boolean isMoving()
    {
        final PlayerSp player = Irrlicht.getMinecraft().getPlayer();
        return player.getStrafe() != 0 ||
               player.getForward() != 0;
    }

    /**
     * Sets the {@link PlayerSp}'s speed.
     *
     * @see PlayerSp
     * @see Irrlicht
     *
     * @param speed the speed the {@link PlayerSp} should be set to.
     */
    public static void setSpeed(double speed)
    {
        if(isMoving())
        {
            final PlayerSp player = Irrlicht.getMinecraft().getPlayer();
            final float forward = player.getForward(),
                        strafe = player.getStrafe();
            final double yaw = Math.toRadians(player.getRotationYaw() + (strafe != 0 ? (forward < 0 ? -1 : 1) * new int[] {
                -90,
                -45,
                90,
                45,
            }[(int)(MathUtils.roundClean(Math.abs(forward), 1) - MathUtils.roundClean(strafe, 1) + 1)] : 0) + (forward < 0 ? -180 : 0));
            player.setMotionX(-Math.sin(yaw) * speed);
            player.setMotionZ(Math.cos(yaw) * speed);
        }
    }
}
