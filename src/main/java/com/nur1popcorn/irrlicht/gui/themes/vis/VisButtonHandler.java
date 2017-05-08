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

import com.nur1popcorn.irrlicht.gui.components.Button;
import com.nur1popcorn.irrlicht.gui.components.layout.Direction;
import com.nur1popcorn.irrlicht.gui.components.layout.Orientation;
import com.nur1popcorn.irrlicht.gui.font.FontRenderer;
import com.nur1popcorn.irrlicht.gui.themes.ComponentHandler;
import com.nur1popcorn.irrlicht.gui.themes.MouseEventType;
import com.nur1popcorn.irrlicht.gui.themes.Theme;
import com.nur1popcorn.irrlicht.gui.themes.parsing.Style;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.ColorValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.NumberValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.StringValue;
import com.nur1popcorn.irrlicht.utils.RenderUtils;

import java.awt.*;

/**
 * The {@link VisButtonHandler} is a simple {@link Button} {@link ComponentHandler}.
 *
 * @see Button
 * @see ComponentHandler
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class VisButtonHandler extends ComponentHandler<Button>
{
    private FontRenderer fontRenderer;
    private Button lastClicked;
    private int backgroundColor,
                hoverColor,
                clickedColor,
                borderColor,
                borderSize,
                fontColor;
    private boolean released;

    public VisButtonHandler(Theme theme)
    {
        super(theme);
    }

    @Override
    public void init()
    {
        final Style style = getStyle("vis-button");
        backgroundColor = ((ColorValue)style.getValue("background-color")).value;
        hoverColor = ((ColorValue)style.getValue("hover-color")).value;
        clickedColor = ((ColorValue)style.getValue("clicked-color")).value;
        borderColor = ((ColorValue)style.getValue("border-color")).value;
        borderSize = ((NumberValue)style.getValue("border-size")).value.intValue();
        fontColor = ((ColorValue)style.getValue("font-color")).value;
        fontRenderer = theme.getFontRenderer(new Font(((StringValue)style.getValue("font-family")).value, Font.PLAIN, ((NumberValue)style.getValue("font-size")).value.intValue()));
    }

    @Override
    public boolean handleInput(Button button, int mouseX, int mouseY, int mouseButton, MouseEventType mouseEventType)
    {
        if(mouseEventType == MouseEventType.PRESSED)
        {
            final float x = button.getX() + button.getMargin(Direction.LEFT),
                        y = button.getY() + button.getMargin(Direction.TOP);
            if(mouseButton == 0 &&
               mouseX >= x &&
               mouseY >= y &&
               mouseX <= x + button.getFullWidth() &&
               mouseY <= y + button.getFullHeight())
            {
                lastClicked = button;
                button.click();
                theme.getGuiManager().focus(button);
                return released = true;
            }
        }
        else
            released = false;
        return false;
    }

    @Override
    public void draw(Button button, int mouseX, int mouseY, float partialTicks)
    {
        final float x = button.getX() + button.getMargin(Direction.LEFT),
                    y = button.getY() + button.getMargin(Direction.TOP);
        RenderUtils.drawRect(
                x,
                y,
                button.getFullWidth(),
                button.getFullHeight(),
                mouseX >= x &&
                mouseY >= y &&
                mouseX <= x + button.getFullWidth() &&
                mouseY <= y + button.getFullHeight() ?
                released && button == lastClicked ? clickedColor : hoverColor : backgroundColor);

        if(button == lastClicked)
        {
            RenderUtils.drawBorder(
                    x,
                    y,
                    button.getFullWidth(),
                    button.getFullHeight(),
                    borderSize * 0.3125f,
                    borderColor);
        }

        if(button.getText() != null)
        {
            final String text = fontRenderer.getStringWidth(button.getText()) > button.getWidth() ?
                    fontRenderer.trimToWidth(button.getText(), button.getWidth() - fontRenderer.getStringWidth("..")) + ".." : button.getText();
            final float offset[] = getPositonOffset(button, new float[] { fontRenderer.getStringHeight(text), fontRenderer.getStringWidth(text) });
            fontRenderer.drawString(
                    text,
                    x + button.getPadding(Direction.LEFT) + offset[Orientation.HORIZONTAL.ordinal()],
                    y + button.getPadding(Direction.TOP) + offset[Orientation.VERTICAL.ordinal()],
                    fontColor);
        }
    }

    @Override
    public String getStyle()
    {
        return ".vis-button {\n" +
               "   -background-color: #ff333333;\n" +
               "   -hover-color: #ff3e3e42;\n" +
               "   -clicked-color: #ff1cb050;\n" +
               "   -border-size: 1;\n" +
               "   -border-color: #ff006496;\n" +
               "   -font-family: 'arial';\n" +
               "   -font-size: 16;\n" +
               "   -font-color: #ffffffff;\n" +
               "}\n";
    }
}
