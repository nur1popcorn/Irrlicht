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

import com.nur1popcorn.irrlicht.gui.components.containers.ScrollContainer;
import com.nur1popcorn.irrlicht.gui.components.layout.Direction;
import com.nur1popcorn.irrlicht.gui.components.layout.Orientation;
import com.nur1popcorn.irrlicht.gui.themes.ContainerHandler;
import com.nur1popcorn.irrlicht.gui.themes.MouseEventType;
import com.nur1popcorn.irrlicht.gui.themes.Theme;
import com.nur1popcorn.irrlicht.gui.themes.parsing.Style;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.ColorValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.NumberValue;
import com.nur1popcorn.irrlicht.utils.RenderUtils;

import static org.lwjgl.opengl.GL11.*;

/**
 * The {@link VisScrollContainer} is a simple {@link ScrollContainer} {@link ContainerHandler}.
 *
 * @see ScrollContainer
 * @see VisScrollContainer
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class VisScrollContainer extends ContainerHandler<ScrollContainer>
{
    private int backgroundColor,
                sliderColor,
                intersectColor,
                sliderSize;

    public VisScrollContainer(Theme theme)
    {
        super(theme);
    }

    @Override
    public void init()
    {
        final Style style = getStyle("vis-scroll-container");
        backgroundColor = ((ColorValue)style.getValue("background-color")).value;
        sliderColor = ((ColorValue)style.getValue("slider-color")).value;
        intersectColor = ((ColorValue)style.getValue("intersect-color")).value;
        ScrollContainer.sliderSize = sliderSize = ((NumberValue)style.getValue("slider-size")).value.intValue();
    }

    @Override
    public boolean handleInput(ScrollContainer scrollContainer, int mouseX, int mouseY, int mouseButton, MouseEventType mouseEventType)
    {
        final float x = scrollContainer.getX() + scrollContainer.getMargin(Direction.LEFT),
                    y = scrollContainer.getY() + scrollContainer.getMargin(Direction.TOP);
        switch(mouseEventType)
        {
            case RELEASED:
                scrollContainer.stopDragging();
                break;
            case PRESSED:
                if(mouseButton == 0)
                {
                    if(scrollContainer.getRealHeight() != 0 &&
                       scrollContainer.getRealHeight() > scrollContainer.getFullHeight() &&
                       mouseX >= x + scrollContainer.getFullWidth() - sliderSize&&
                       mouseY >= y &&
                       mouseX <= x + scrollContainer.getFullWidth() &&
                       mouseY <= y + scrollContainer.getFullHeight() - sliderSize)
                    {
                        final float scrollbarHeight = (scrollContainer.getFullHeight() - sliderSize) * (scrollContainer.getFullHeight() / scrollContainer.getRealHeight());
                        if(mouseY >= y + scrollContainer.getScroll(Orientation.VERTICAL) &&
                           mouseY <= y + scrollContainer.getScroll(Orientation.VERTICAL) + scrollbarHeight)
                            scrollContainer.setDragging(Orientation.VERTICAL);
                        else
                            scrollContainer.setScroll(
                                    Orientation.VERTICAL,
                                    mouseY - y - scrollbarHeight / 2 < 0 ?
                                    0 : mouseY - y + scrollbarHeight / 2 > scrollContainer.getFullHeight() - sliderSize ?
                                    scrollContainer.getFullHeight() - sliderSize - scrollbarHeight : mouseY - y - scrollbarHeight / 2);
                        scrollContainer.layoutComponents();
                        return true;
                    }

                    if(scrollContainer.getRealWidth() != 0 &&
                       scrollContainer.getRealWidth() > scrollContainer.getFullWidth() &&
                       mouseX >= x &&
                       mouseY >= y + scrollContainer.getFullHeight() - sliderSize &&
                       mouseX <= x + scrollContainer.getFullWidth() - sliderSize &&
                       mouseY <= y + scrollContainer.getFullHeight())
                    {
                        final float scrollbarWidth = (scrollContainer.getFullWidth() - sliderSize) * (scrollContainer.getFullWidth() / scrollContainer.getRealWidth());
                        if(mouseX >= x + scrollContainer.getScroll(Orientation.HORIZONTAL) &&
                           mouseX <= x + scrollContainer.getScroll(Orientation.HORIZONTAL) + scrollbarWidth)
                            scrollContainer.setDragging(Orientation.HORIZONTAL);
                        else
                            scrollContainer.setScroll(
                                    Orientation.HORIZONTAL,
                                    mouseX - x - scrollbarWidth / 2 < 0 ?
                                    0 : mouseX - x + scrollbarWidth / 2 > scrollContainer.getFullWidth() - sliderSize ?
                                    scrollContainer.getFullWidth() - sliderSize - scrollbarWidth : mouseX - x - scrollbarWidth / 2);
                        scrollContainer.layoutComponents();
                        return true;
                    }
                }
                break;
        }

        if(mouseX >= x &&
           mouseX <= x + scrollContainer.getFullWidth() &&
           mouseY >= y &&
           mouseY <= y + scrollContainer.getFullHeight())
        {
            super.handleInput(scrollContainer, mouseX, mouseY, mouseButton, mouseEventType);
            return true;
        }
        return false;
    }

    @Override
    public void draw(ScrollContainer scrollContainer, int mouseX, int mouseY, float partialTicks)
    {
        final float x = scrollContainer.getX() + scrollContainer.getMargin(Direction.LEFT),
                    y = scrollContainer.getY() + scrollContainer.getMargin(Direction.TOP);
        if(scrollContainer.isDragging())
        {
            switch(scrollContainer.getDrag())
            {
                case VERTICAL:
                    final float scrollbarHeight = (scrollContainer.getFullHeight() - sliderSize) * (scrollContainer.getFullHeight() / scrollContainer.getRealHeight());
                    scrollContainer.setScroll(
                            Orientation.VERTICAL,
                            mouseY - y - scrollbarHeight / 2 < 0 ?
                            0 : mouseY - y + scrollbarHeight / 2 > scrollContainer.getFullHeight() - sliderSize ?
                            scrollContainer.getFullHeight() - sliderSize - scrollbarHeight : mouseY - y - scrollbarHeight / 2);
                    break;
                case HORIZONTAL:
                    final float scrollbarWidth = (scrollContainer.getFullWidth() - sliderSize) * (scrollContainer.getFullWidth() / scrollContainer.getRealWidth());
                    scrollContainer.setScroll(
                            Orientation.HORIZONTAL,
                            mouseX - x - scrollbarWidth / 2 < 0 ?
                            0 : mouseX - x + scrollbarWidth / 2 > scrollContainer.getFullWidth() - sliderSize ?
                            scrollContainer.getFullWidth() - sliderSize - scrollbarWidth : mouseX - x - scrollbarWidth / 2);
                    break;
            }
            scrollContainer.layoutComponents();
        }

        final boolean vertical = (scrollContainer.getRealHeight() != 0 &&
                                  scrollContainer.getRealHeight() > scrollContainer.getFullHeight()),
                      horizontal = (scrollContainer.getRealWidth() != 0 &&
                                    scrollContainer.getRealWidth() > scrollContainer.getFullWidth());
        if(vertical)
        {
            RenderUtils.drawRect(
                    x + scrollContainer.getFullWidth() - sliderSize,
                    y,
                    sliderSize,
                    scrollContainer.getFullHeight() - sliderSize,
                    backgroundColor);
            RenderUtils.drawRect(
                    x + scrollContainer.getFullWidth() - sliderSize,
                    y + scrollContainer.getScroll(Orientation.VERTICAL),
                    sliderSize,
                    (scrollContainer.getFullHeight() - sliderSize) * (scrollContainer.getFullHeight() / scrollContainer.getRealHeight()),
                    sliderColor);
        }

        if(horizontal)
        {
            RenderUtils.drawRect(
                    x,
                    y + scrollContainer.getFullHeight() - sliderSize,
                    vertical ? scrollContainer.getFullWidth() - sliderSize : scrollContainer.getFullWidth(),
                    sliderSize,
                    backgroundColor);
            RenderUtils.drawRect(
                    x + scrollContainer.getScroll(Orientation.HORIZONTAL),
                    y + scrollContainer.getFullHeight() - sliderSize,
                    (scrollContainer.getFullWidth() - sliderSize) * (scrollContainer.getFullWidth() / scrollContainer.getRealWidth()),
                    sliderSize,
                    sliderColor);
        }

        if(vertical || horizontal)
           RenderUtils.drawRect(
                   x + scrollContainer.getFullWidth() - sliderSize,
                   y + scrollContainer.getFullHeight() - sliderSize,
                   sliderSize,
                   sliderSize,
                   intersectColor);

        glPushAttrib(GL_SCISSOR_BIT);
        RenderUtils.prepareScissorBox(
                x,
                y,
                x + scrollContainer.getFullWidth() - (vertical ? sliderSize : 0),
                y + scrollContainer.getFullHeight() - (horizontal ? sliderSize : 0));
        super.draw(scrollContainer, mouseX, mouseY, partialTicks);
        glPopAttrib();
    }

    @Override
    public String getStyle()
    {
        return ".vis-scroll-container {\n" +
               "   -background-color: #ff333333;\n" +
               "   -slider-color: #ff717171;\n" +
               "   -intersect-color: #ff444444;" +
               "   -slider-size: 4;\n" +
               "}\n";
    }
}
