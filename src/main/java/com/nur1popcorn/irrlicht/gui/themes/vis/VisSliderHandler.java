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

import com.nur1popcorn.irrlicht.gui.components.Slider;
import com.nur1popcorn.irrlicht.gui.components.layout.Direction;
import com.nur1popcorn.irrlicht.gui.themes.ComponentHandler;
import com.nur1popcorn.irrlicht.gui.themes.MouseEventType;
import com.nur1popcorn.irrlicht.gui.themes.Theme;
import com.nur1popcorn.irrlicht.gui.themes.parsing.Style;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.ColorValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.NumberValue;
import com.nur1popcorn.irrlicht.utils.NumberUtils;
import com.nur1popcorn.irrlicht.utils.RenderUtils;

/**
 * The {@link VisSliderHandler} is a simple {@link Slider} {@link ComponentHandler}.
 *
 * @see Slider
 * @see ComponentHandler
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class VisSliderHandler extends ComponentHandler<Slider>
{
    private int backgroundColor,
                foregroundColor,
                hoverColor,
                clickedColor,
                lockedColor,
                borderColor,
                borderSize;

    public VisSliderHandler(Theme theme)
    {
        super(theme);
    }

    @Override
    public void init()
    {
        final Style style = getStyle("vis-slider");
        assert style != null;
        backgroundColor = ((ColorValue)style.getValue("background-color")).value;
        foregroundColor = ((ColorValue)style.getValue("foreground-color")).value;
        hoverColor = ((ColorValue)style.getValue("hover-color")).value;
        clickedColor = ((ColorValue)style.getValue("clicked-color")).value;
        lockedColor = ((ColorValue)style.getValue("locked-color")).value;
        borderColor = ((ColorValue)style.getValue("border-color")).value;
        borderSize = ((NumberValue)style.getValue("border-size")).value.intValue();
    }

    @Override
    public boolean handleInput(Slider slider, int mouseX, int mouseY, int mouseButton, MouseEventType mouseEventType)
    {
        final float x = slider.getX() + slider.getMargin(Direction.LEFT),
                    y = slider.getY() + slider.getMargin(Direction.TOP);
        if(mouseButton == 0)
        {
            if(mouseEventType == MouseEventType.PRESSED)
            {
                if(mouseX >= x &&
                   mouseY >= y &&
                   mouseX <= x + slider.getFullWidth() &&
                   mouseY <= y + slider.getFullHeight())
                {
                    switch(slider.getOrientation())
                    {
                        case VERTICAL:
                        {
                            float sliderX = x + slider.getFullWidth() / 2 - 0.3125f - 4,
                                  sliderY = y + slider.getFullHeight() * (slider.getValue().floatValue() - slider.getMin().floatValue())  / (slider.getMax().floatValue() - slider.getMin().floatValue()) - 2;
                            if(mouseX >= sliderX &&
                               mouseY >= sliderY &&
                               mouseX <= sliderX + 4.3125f &&
                               mouseY <= sliderY + 8.6250f)
                                slider.setDragging(true);
                            else
                                slider.setValue(
                                        NumberUtils.convertToTarget(Math.min(Math.max(
                                                slider.getIncrement().floatValue() * Math.round(((mouseY - y) / slider.getFullHeight() * (slider.getMax().floatValue() - slider.getMin().floatValue())) / slider.getIncrement().floatValue()) + slider.getMin().floatValue(),
                                                slider.getMin().floatValue()),
                                                slider.getMax().floatValue()), slider.getNumberType()));
                        }
                        break;
                        case HORIZONTAL:
                        {
                            float sliderX = x + slider.getFullWidth() * (slider.getValue().floatValue() - slider.getMin().floatValue())  / (slider.getMax().floatValue() - slider.getMin().floatValue()) - 2,
                                  sliderY = y + slider.getFullHeight() / 2 - 0.3125f - 4;
                            if(mouseX >= sliderX &&
                               mouseY >= sliderY &&
                               mouseX <= sliderX + 8.6250f &&
                               mouseY <= sliderY + 4.3125f)
                                slider.setDragging(true);
                            else
                                slider.setValue(
                                        NumberUtils.convertToTarget(Math.min(Math.max(
                                                slider.getIncrement().floatValue() * Math.round(((mouseX - x) / slider.getFullWidth() * (slider.getMax().floatValue() - slider.getMin().floatValue())) / slider.getIncrement().floatValue()) + slider.getMin().floatValue(),
                                                slider.getMin().floatValue()),
                                                slider.getMax().floatValue()), slider.getNumberType()));
                        }
                    }
                    return true;
                }
            }
            else
                slider.setDragging(false);
        }
        return false;
    }

    @Override
    public void draw(Slider slider, int mouseX, int mouseY, float partialTicks)
    {
        final float x = slider.getX() + slider.getMargin(Direction.LEFT),
                    y = slider.getY() + slider.getMargin(Direction.TOP);
        if(slider.isDragging())
            switch(slider.getOrientation())
            {
                case VERTICAL:
                    slider.setValue(
                            NumberUtils.convertToTarget(Math.min(Math.max(
                                    slider.getIncrement().floatValue() * Math.round(((mouseY - y) / slider.getFullHeight() * (slider.getMax().floatValue() - slider.getMin().floatValue())) / slider.getIncrement().floatValue()) + slider.getMin().floatValue(),
                                    slider.getMin().floatValue()),
                                    slider.getMax().floatValue()), slider.getNumberType()));
                    break;
                case HORIZONTAL:
                    slider.setValue(
                            NumberUtils.convertToTarget(Math.min(Math.max(
                                    slider.getIncrement().floatValue() * Math.round(((mouseX - x) / slider.getFullWidth() * (slider.getMax().floatValue() - slider.getMin().floatValue())) / slider.getIncrement().floatValue()) + slider.getMin().floatValue(),
                                    slider.getMin().floatValue()),
                                    slider.getMax().floatValue()), slider.getNumberType()));
                    break;
            }

        switch(slider.getOrientation())
        {
            case VERTICAL:
                RenderUtils.drawRect(
                        x + slider.getFullWidth() / 2 - 0.3125f,
                        y,
                        0.3125f,
                        slider.getFullHeight(),
                        backgroundColor);
                RenderUtils.drawRect(
                        x + slider.getFullWidth() / 2 - 0.3125f - 3,
                        y + slider.getFullHeight() * (slider.getValue().floatValue() - slider.getMin().floatValue())  / (slider.getMax().floatValue() - slider.getMin().floatValue()) - 1,
                        6.3125f,
                        2,
                        foregroundColor);
                break;
            case HORIZONTAL:
                RenderUtils.drawRect(
                        x,
                        y + slider.getFullHeight() / 2 - 0.3125f,
                        slider.getFullWidth(),
                        0.3125f,
                        backgroundColor);
                RenderUtils.drawRect(
                        x + slider.getFullWidth() * (slider.getValue().floatValue() - slider.getMin().floatValue())  / (slider.getMax().floatValue() - slider.getMin().floatValue()) - 1,
                        y + slider.getFullHeight() / 2 - 0.3125f - 3,
                        2,
                        6.3125f,
                        foregroundColor);
                break;
        }
    }

    @Override
    public String getStyle()
    {
        return ".vis-slider {\n" +
                "   -background-color: #ff363636;\n" +
                "   -foreground-color: #ff555555;\n" +
                "   -hover-color: #ff3e3e42;\n" +
                "   -clicked-color: #ff008fd9;\n" +
                "   -locked-color: #88252525;\n" +
                "   -border-size: 1;\n" +
                "   -border-color: #ff006496;\n" +
                "}\n";
    }
}
