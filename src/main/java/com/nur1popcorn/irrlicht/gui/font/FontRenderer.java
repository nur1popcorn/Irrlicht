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

package com.nur1popcorn.irrlicht.gui.font;

import com.nur1popcorn.irrlicht.utils.RenderUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * The {@link FontRenderer} is used to generate textures for fonts and draw them to the screen.
 *
 * @see com.nur1popcorn.irrlicht.gui.themes.ComponentHandler
 * @see com.nur1popcorn.irrlicht.gui.themes.Theme
 * @see RenderUtils
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class FontRenderer
{
    private static final int TEXTURE_SIZE = 512;
    private Rectangle2D characterBounds[] = new Rectangle2D[256];
    private Font font;
    private int texture;
    private float maxHeight;
    private boolean freed;

    public FontRenderer(Font font)
    {
        this.font = font;
        texture = generateTexture(font);
    }

    /**
     * Generates a texture for the font and also stores the bounds.
     *
     * @param font the font for which the texture and bounds are supposed to get generated.
     *
     * @return the texture used to draw the font.
     */
    private int generateTexture(Font font)
    {
        BufferedImage bufferedImage = new BufferedImage(TEXTURE_SIZE, TEXTURE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.setFont(font);
        Map desktopHints = (Map) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
        if(desktopHints != null)
            graphics.setRenderingHints(desktopHints);
        final FontMetrics fontMetrics = graphics.getFontMetrics();
        float highestChar = 0,
              x = 0,
              y = 0;
        for(int i = 0; i < characterBounds.length; i++)
        {
            char c = (char) i;
            Rectangle2D rect = fontMetrics.getStringBounds(Character.toString(c), graphics);
            final float width = (float)rect.getWidth() + 2;
            if(x + width >= TEXTURE_SIZE)
            {
                y += highestChar;
                x = highestChar = 0;
            }
            if(rect.getHeight() > highestChar)
            {
                highestChar = (float)rect.getHeight();
                if(highestChar > maxHeight)
                    maxHeight = highestChar;
            }
            graphics.drawString(Character.toString(c), x, y + fontMetrics.getAscent());
            characterBounds[i] = new Rectangle2D.Double(x, y, rect.getWidth() + 1, rect.getHeight());
            x += width;
        }
        freed = false;
        return RenderUtils.createTexture(bufferedImage);
    }

    /**
     * Draws given character at given location.
     *
     * @param c the character which is supposed to be drawn.
     * @param x the x position.
     * @param y the y position.
     */
    public void drawChar(char c, float x, float y)
    {
        if(freed)
            return;
        RenderUtils.drawRectWithTexture(x, y, (float)characterBounds[c].getWidth(), (float)characterBounds[c].getHeight(), (float)characterBounds[c].getX(), (float)characterBounds[c].getY(), TEXTURE_SIZE, TEXTURE_SIZE);
    }

    /**
     * Draws a string at given location.
     *
     * @param s the string which is supposed to be drawn.
     * @param x the x position.
     * @param y the y position.
     * @param color the color used to draw the string.
     */
    public void drawString(String s, float x, float y, int color)
    {
        if(freed)
            return;
        RenderUtils.glColor(color);
        glPushMatrix();
        glScaled(0.25, 0.25, 1);

        glEnable(GL_BLEND);
        glBindTexture(GL_TEXTURE_2D, texture);
        float posX = x * 4;
        for(char c : s.toCharArray())
        {
            drawChar(c, posX, y * 4);
            posX += (float)characterBounds[c].getWidth();
        }
        RenderUtils.disable2D();
        glPopMatrix();
    }

    /**
     * Draws a string at given location with a shadow.
     *
     * @param s the string which is supposed to be drawn.
     * @param x the x position.
     * @param y the y position.
     * @param color the color used to draw the string.
     *
     * @see #drawString(String, float, float, int)
     */
    public void drawStringWithShadow(String s, float x, float y, int color)
    {
        if(freed)
            return;
        drawString(s, x + 2, y + 2, 0xff000000);
        drawString(s, x, y, color);
    }

    /**
     * @param c the char of which the width should be returned.
     *
     * @return the characters width.
     */
    public float getCharWidth(char c)
    {
        return (float)characterBounds[c].getWidth() * 0.25f;
    }

    /**
     * @param c the char of which the height should be returned.
     *
     * @return the characters height.
     */
    public float getCharHeight(char c)
    {
        return (float)characterBounds[c].getHeight() * 0.25f;
    }

    /**
     * @param s the string of which the width should be returned.
     *
     * @return the strings width.
     */
    public float getStringWidth(String s)
    {
        float size = 0;
        for(char c : s.toCharArray())
            size += getCharWidth(c);
        return size;
    }

    /**
     * Trims the string provided to the width provided.
     *
     * @param s the string which is supposed to trimmed.
     * @param width the width the string is supposed to be trimmed to.
     *
     * @return the string trimmed to the width provided.
     */
    public String trimToWidth(String s, float width)
    {
        int i = s.length() - 1;
        for(float strWidth = getStringWidth(s); i >= 0; i--)
            if(strWidth > width)
                strWidth -= getCharWidth(s.charAt(i));
            else
                break;
        return s.substring(0, i + 1);
    }

    /**
     * @param s the string of which the dimensions for each character are supposed to be returned;
     *
     * @return the dimensions of each character in the string.
     */
    public float[][] getDimensions(String s)
    {
        float dimensions[][] = new float[s.length()][2];
        final char chars[] = s.toCharArray();
        for(int i = 0; i < chars.length; i++)
        {
            dimensions[i][0] = getCharWidth(chars[i]);
            dimensions[i][1] = getCharHeight(chars[i]);
        }
        return dimensions;
    }

    /**
     * @param s the string of which the height should be returned.
     *
     * @return the strings height.
     */
    public float getStringHeight(String s)
    {
        float highest = 0;
        for(char c : s.toCharArray())
        {
            final float height = getCharHeight(c);
            if(height > highest)
                highest = height;
        }
        return highest;
    }

    /**
     * Frees the fonts textures.
     */
    public void free()
    {
        if(freed)
            return;
        glBindTexture(GL_TEXTURE_2D, 0);
        glDeleteTextures(texture);
        freed = true;
    }

    /**
     * @return whether or not the textures were freed.
     */
    public boolean isFreed()
    {
        return freed;
    }

    /**
     * Loads a new font and also frees old one.
     *
     * @param font the font which is supposed to be loaded.
     */
    public void setFont(Font font)
    {
        this.font = font;
        free();
        this.texture = generateTexture(font);
    }

    /**
     * @return the currently loaded font.
     */
    public Font getFont()
    {
        return font;
    }

    /**
     * @return the texture id for the font.
     */
    public int getTexture()
    {
        return texture;
    }

    /**
     * @return the max character height of the font.
     */
    public float getMaxHeight()
    {
        return maxHeight / 4;
    }
}
