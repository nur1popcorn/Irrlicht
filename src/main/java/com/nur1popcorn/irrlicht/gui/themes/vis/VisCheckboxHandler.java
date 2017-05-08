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

package com.nur1popcorn.irrlicht.gui.themes.vis;

import com.nur1popcorn.irrlicht.gui.components.Checkbox;
import com.nur1popcorn.irrlicht.gui.components.layout.Direction;
import com.nur1popcorn.irrlicht.gui.themes.ComponentHandler;
import com.nur1popcorn.irrlicht.gui.themes.MouseEventType;
import com.nur1popcorn.irrlicht.gui.themes.Theme;
import com.nur1popcorn.irrlicht.gui.themes.parsing.Style;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.ColorValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.NumberValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.StringValue;
import com.nur1popcorn.irrlicht.utils.RenderUtils;

import javax.imageio.ImageIO;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

/**
 * The {@link VisCheckboxHandler} is a simple {@link Checkbox} {@link ComponentHandler}.
 *
 * @see Checkbox
 * @see ComponentHandler
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class VisCheckboxHandler extends ComponentHandler<Checkbox>
{
    private Checkbox lastClicked;
    private int backgroundColor,
                iconColor,
                borderColor,
                clickedColor,
                lockedColor,
                borderSize;
    private int icon;
    private boolean released;

    public VisCheckboxHandler(Theme theme)
    {
        super(theme);
    }

    @Override
    public void init()
    {
        final Style style = getStyle("vis-checkbox");
        assert style != null;
        backgroundColor = ((ColorValue)style.getValue("background-color")).value;
        iconColor = ((ColorValue)style.getValue("icon-color")).value;
        borderColor = ((ColorValue)style.getValue("border-color")).value;
        clickedColor = ((ColorValue)style.getValue("clicked-color")).value;
        lockedColor = ((ColorValue)style.getValue("locked-color")).value;
        borderSize = ((NumberValue)style.getValue("border-size")).value.intValue();
        try
        {
            icon = RenderUtils.createTexture(ImageIO.read(VisCheckboxHandler.class.getResourceAsStream(((StringValue)style.getValue("icon")).value)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void free()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
        glDeleteTextures(icon);
    }

    @Override
    public boolean handleInput(Checkbox checkbox, int mouseX, int mouseY, int mouseButton, MouseEventType mouseEventType)
    {
        final float x = checkbox.getX() + checkbox.getMargin(Direction.LEFT),
                    y = checkbox.getY() + checkbox.getMargin(Direction.TOP);
        if(mouseButton == 0)
            switch(mouseEventType)
            {
                case RELEASED:
                    released = false;
                    break;
                case PRESSED:
                    if(!checkbox.isLocked() &&
                       mouseX >= x &&
                       mouseY >= y &&
                       mouseX <= x + checkbox.getFullWidth() &&
                       mouseY <= y + checkbox.getFullHeight())
                    {
                        (lastClicked = checkbox).setChecked(!checkbox.isChecked());
                        theme.getGuiManager().focus(checkbox);
                        return released = true;
                    }
                    break;
            }
        return false;
    }

    @Override
    public void draw(Checkbox checkbox, int mouseX, int mouseY, float partialTicks)
    {
        glEnable(GL_BLEND);
        final float x = checkbox.getX() + checkbox.getMargin(Direction.LEFT),
                    y = checkbox.getY() + checkbox.getMargin(Direction.TOP);
        RenderUtils.drawRect(
                x,
                y,
                checkbox.getFullWidth(),
                checkbox.getFullHeight(),
                released && checkbox == lastClicked ? clickedColor : backgroundColor);

        if(lastClicked == checkbox)
            RenderUtils.drawBorder(x, y, checkbox.getFullWidth(), checkbox.getFullHeight(), 0.3125f * borderSize, borderColor);

        if(checkbox.isChecked())
        {
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, icon);
            RenderUtils.glColor(iconColor);
            RenderUtils.drawRectWithTexture(
                    x + 0.3125f * (borderSize + 1) + checkbox.getPadding(Direction.LEFT),
                    y + 0.3125f * (borderSize + 1) + checkbox.getPadding(Direction.TOP),
                    checkbox.getWidth() - 1,
                    checkbox.getHeight() - 1,
                    0,
                    0,
                    checkbox.getWidth() - 1,
                    checkbox.getHeight() - 1);
        }

        if(checkbox.isLocked())
            RenderUtils.drawRect(x, y, checkbox.getFullWidth(), checkbox.getFullHeight(), lockedColor);

        RenderUtils.disable2D();
    }

    @Override
    public String getStyle()
    {
        return ".vis-checkbox {\n" +
               "   -background-color: #ff333333;\n" +
               "   -icon-color: #ffffffff;\n" +
               "   -border-color: #ff006496;\n" +
               "   -clicked-color: #ff008fd9;\n" +
               "   -locked-color: #88252525;\n" +
               "   -border-size: 1;\n" +
               "   -icon: '/icons/checkmark.png';\n" +
               "}\n";
    }
}
