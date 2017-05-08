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

import com.nur1popcorn.irrlicht.gui.components.Label;
import com.nur1popcorn.irrlicht.gui.font.FontRenderer;
import com.nur1popcorn.irrlicht.gui.themes.ComponentHandler;
import com.nur1popcorn.irrlicht.gui.themes.Theme;
import com.nur1popcorn.irrlicht.gui.themes.parsing.Style;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.ColorValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.NumberValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.StringValue;

import java.awt.*;

/**
 * The {@link VisLabelHandler} is a simple {@link Label} {@link ComponentHandler}.
 *
 * @see Label
 * @see ComponentHandler
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class VisLabelHandler extends ComponentHandler<Label>
{
    private int color;
    private FontRenderer fontRenderer;

    public VisLabelHandler(Theme theme)
    {
        super(theme);
    }

    @Override
    public void init()
    {
        final Style style = getStyle("vis-label");
        color = ((ColorValue)style.getValue("font-color")).value;
        Label.fontRenderer = fontRenderer = theme.getFontRenderer(new Font(((StringValue)style.getValue("font-family")).value, Font.PLAIN, ((NumberValue)style.getValue("font-size")).value.intValue()));
    }

    @Override
    public void draw(Label label, int mouseX, int mouseY, float partialTicks)
    {
        fontRenderer.drawString(label.getText(), label.getX(), label.getY(), color);
    }

    @Override
    public String getStyle()
    {
        return ".vis-label {\n" +
                "   -font-color: #ffffffff;\n" +
                "   -font-family: 'Arial';\n" +
                "   -font-size: 16;\n" +
                "}\n";
    }
}
