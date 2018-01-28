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
import com.nur1popcorn.irrlicht.management.ScaledResolution;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

/**
 * The {@link RenderUtils} is an util providing all sorts of OpenGl helpers.
 *
 * @see ScaledResolution
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class RenderUtils
{
    //prevent construction :/
    private RenderUtils()
    {}

    /**
     * Enables all kinds of capabilities useful for drawing in 2D.
     */
    public static void enable2D()
    {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
    }

    /**
     * Disables all capabilities enabled by {@link #enable2D()}.
     *
     * @see #enable2D()
     */
    public static void disable2D()
    {
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    /**
     * Draws a rectangle at given position with provided width, height and color.
     *
     * @param x the x position at which the rectangle should be drawn.
     * @param y the y position at which the rectangle should be drawn.
     * @param width the width used to draw the rectangle.
     * @param height the height used to draw the rectangle.
     * @param color the color in which the rectangle should be drawn.
     */
    public static void drawRect(float x, float y, float width, float height, int color)
    {
        enable2D();
        glColor(color);
        glBegin(GL_QUADS);
        glVertex2f(x, y + height);
        glVertex2f(x + width, y + height);
        glVertex2f(x + width, y);
        glVertex2f(x, y);
        glEnd();
        disable2D();
    }

    /**
     * Draws a border at the given position with the given width, height, size and color.
     *
     * @param x the x position at which the border should be drawn.
     * @param y the y position at which the border should be drawn.
     * @param width the width used to draw the border.
     * @param height the height used to draw the border.
     * @param size the size of the border.
     * @param color the color in which the border should be drawn.
     */
    public static void drawBorder(float x, float y, float width, float height, float size, int color)
    {
        drawRect(x, y, width, size, color);
        drawRect(x, y, size, height, color);
        drawRect(x + width - size, y, size, height, color);
        drawRect(x, y + height - size, width, size, color);
    }

    /**
     * Draws a rectangle at given position with provided with, height, color and border
     * with given size and color.
     *
     * @param x the x position at which the rectangle should be drawn.
     * @param y the y position at which the rectangle should be drawn.
     * @param width the width used to draw the rectangle.
     * @param height the height used to draw the rectangle.
     * @param size the size of the border.
     * @param color the color in which the rectangle should be drawn.
     * @param borderColor the color of the border.
     */
    public static void drawBorderRect(float x, float y, float width, float height, float size, int color, int borderColor)
    {
        drawRect(x, y, width, height, color);
        drawBorder(x, y, width, height, size, borderColor);
    }

    /**
     * Draws a line for one position to another using the given width and color.
     *
     * @param x the x start of the line.
     * @param y the y start of the line.
     * @param otherX the x end of the line.
     * @param otherY the y end of the line.
     * @param width the width of the line.
     * @param color the color of the line.
     */
    public static void drawLine(float x, float y, float otherX, float otherY, int width, int color)
    {
        enable2D();
        glColor(color);
        glLineWidth(width);
        glBegin(GL_LINES);
        glVertex2f(x, y);
        glVertex2f(otherX, otherY);
        glEnd();
        disable2D();
    }

    /**
     * Creates a texture from the {@link BufferedImage} provided.
     *
     * @param bufferedImage the image from which the texture is created.
     *
     * @return the glTextureID.
     */
    public static int createTexture(BufferedImage bufferedImage)
    {
        final int pixels[] = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, 0, bufferedImage.getWidth());

        final ByteBuffer buffer = BufferUtils.createByteBuffer(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);
        for(int y = 0; y < bufferedImage.getHeight(); y++)
            for(int x = 0; x < bufferedImage.getWidth(); x++)
            {
                final int pixel = pixels[y * bufferedImage.getWidth() + x];
                buffer.put((byte)((pixel >> 16) & 0xFF));
                buffer.put((byte)((pixel >> 8) & 0xFF));
                buffer.put((byte)(pixel & 0xFF));
                buffer.put((byte)((pixel >> 24) & 0xFF));
            }
        buffer.flip();

        final int texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        return texId;
    }

    /**
     * Draws a rectangle with the currently bound texture.
     *
     * @param x the x position at which the rectangle should be drawn.
     * @param y the y position at which the rectangle should be drawn.
     * @param width the width used to draw the rectangle.
     * @param height the height used to draw the rectangle.
     * @param textureX the texture x offset.
     * @param textureY the texture y offset.
     * @param fullTextureWidth the texture's scaled width.
     * @param fullTextureHeight the texture's scaled height.
     */
    public static void drawRectWithTexture(float x, float y, float width, float height, float textureX, float textureY, float fullTextureWidth, float fullTextureHeight)
    {
        final float texX = textureX / fullTextureWidth;
        final float texY = textureY / fullTextureHeight;
        final float texWidth = width / fullTextureWidth;
        final float texHeight = height / fullTextureHeight;
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_TRIANGLES);
        glTexCoord2f(texX + texWidth, texY);
        glVertex2d(x + width, y);
        glTexCoord2f(texX, texY);
        glVertex2d(x, y);
        glTexCoord2f(texX, texY + texHeight);
        glVertex2d(x, y + height);
        glTexCoord2f(texX, texY + texHeight);
        glVertex2d(x, y + height);
        glTexCoord2f(texX + texWidth, texY + texHeight);
        glVertex2d(x + width, y + height);
        glTexCoord2f(texX + texWidth, texY);
        glVertex2d(x + width, y);
        glEnd();
    }

    /**
     * Sets the current color.
     *
     * @param color the color that is supposed to be set.
     */
    public static void glColor(int color)
    {
        glColor4f(
            (color >> 16 & 255) / 255f,
            (color >> 8 & 255) / 255f,
            (color & 255) / 255f,
            (color >> 24 & 255) / 255f
        );
    }

    /**
     * @param r the red value.
     * @param g the green value.
     * @param b the blue value.
     * @param a the alpha value.
     *
     * @return the values as a hex color code.
     */
    public static int getColor(int r, int g, int b, int a)
    {
        return (a << 24) + (r << 16) + (g << 8) + (b);
    }

    /**
     * Creates a scissor box at the given location.
     *
     * @param x the x position.
     * @param y the y position.
     * @param width the width.
     * @param height the height.
     *
     * @see ScaledResolution
     */
    public static void prepareScissorBox(float x, float y, float width, float height)
    {
        final ScaledResolution scaledResolution = new ScaledResolution(Irrlicht.getMinecraft().getGameSettings());
        glScissor(
            (int)(x * scaledResolution.getScaleFactor()),
            (int)(Display.getHeight() - height * scaledResolution.getScaleFactor() - y * scaledResolution.getScaleFactor()),
            (int)(width * scaledResolution.getScaleFactor()),
            (int)(height * scaledResolution.getScaleFactor())
        );
    }

}
