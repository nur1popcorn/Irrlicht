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

import com.nur1popcorn.irrlicht.engine.wrappers.client.settings.GameSettings;
import org.lwjgl.opengl.Display;

/**
 * The {@link ScaledResolution} is a class based on Minecraft's sourcecode. It's used to
 * position elements on the scaled viewport.
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class ScaledResolution
{
    private int scaleFactor = 1,
                scaledWidth = Display.getWidth(),
                scaledHeight = Display.getHeight();

    public ScaledResolution(GameSettings gameSettings)
    {
        int guiScale = (guiScale = gameSettings.getGuiScale()) == 0 ? 1000 : guiScale;
        while(scaleFactor < guiScale &&
              scaledWidth / (scaleFactor + 1) >= 320 &&
              scaledHeight / (scaleFactor + 1) >= 240)
            scaleFactor++;
        scaledWidth = scaledWidth / scaleFactor;
        scaledHeight = scaledHeight / scaleFactor;
    }

    /**
     * @return by how much the viewport was scaled down.
     */
    public int getScaleFactor()
    {
        return scaleFactor;
    }

    /**
     * @return the scaled viewport's width.
     */
    public int getScaledWidth()
    {
        return scaledWidth;
    }

    /**
     * @return the scaled viewport's height.
     */
    public int getScaledHeight()
    {
        return scaledHeight;
    }
}
