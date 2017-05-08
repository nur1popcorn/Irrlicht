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

import com.nur1popcorn.irrlicht.gui.components.containers.Frame;
import com.nur1popcorn.irrlicht.gui.components.layout.Direction;
import com.nur1popcorn.irrlicht.gui.font.FontRenderer;
import com.nur1popcorn.irrlicht.gui.themes.ContainerHandler;
import com.nur1popcorn.irrlicht.gui.themes.MouseEventType;
import com.nur1popcorn.irrlicht.gui.themes.Theme;
import com.nur1popcorn.irrlicht.gui.themes.parsing.Style;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.ColorValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.NumberValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.StringValue;
import com.nur1popcorn.irrlicht.utils.RenderUtils;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * The {@link VisTextBoxHandler} is a simple {@link Frame} {@link ContainerHandler}.
 *
 * @see Frame
 * @see ContainerHandler
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class VisFrameHandler extends ContainerHandler<Frame>
{
    private FontRenderer fontRenderer;
    private int backgroundColor,
                draggableColor,
                borderColor,
                borderSize,
                fontColor;
    private float draggableHeight;

    public VisFrameHandler(Theme theme)
    {
        super(theme);
    }

    @Override
    public void init()
    {
        final Style style = getStyle("vis-frame");
        backgroundColor = ((ColorValue)style.getValue("background-color")).value;
        draggableColor = ((ColorValue)style.getValue("draggable-color")).value;
        borderSize = ((NumberValue)style.getValue("border-size")).value.intValue();
        borderColor = ((ColorValue)style.getValue("border-color")).value;
        draggableHeight = ((NumberValue)style.getValue("draggable-height")).value.intValue();
        fontColor = ((ColorValue)style.getValue("font-color")).value;
        fontRenderer = theme.getFontRenderer(new Font(((StringValue)style.getValue("font-family")).value, Font.PLAIN, ((NumberValue)style.getValue("font-size")).value.intValue()));
    }

    @Override
    public boolean handleInput(Frame frame, int mouseX, int mouseY, int mouseButton, MouseEventType mouseEventType)
    {
        final float x = frame.getX() + frame.getMargin(Direction.LEFT),
                    y = frame.getY() - draggableHeight + frame.getMargin(Direction.TOP);
        if(mouseButton == 0)
        {
            if(mouseEventType == MouseEventType.PRESSED &&
               mouseX >= x &&
               mouseY >= y &&
               mouseX <= x + frame.getFullWidth())
            {
                if(mouseY <= y + draggableHeight)
                {
                    frame.setDragging(true);
                    frame.setDragX(mouseX - frame.getX());
                    frame.setDragY(mouseY - frame.getY());
                    theme.getGuiManager().focus(frame);
                    return true;
                }
                else if(mouseY <= y + frame.getFullHeightNeeded())
                {
                    super.handleInput(
                            frame,
                            mouseX,
                            mouseY,
                            mouseButton,
                            mouseEventType);
                    return true;
                }
            }
            else
                frame.setDragging(false);
        }
        return super.handleInput(
                frame,
                mouseX,
                mouseY,
                mouseButton,
                mouseEventType);
    }

    @Override
    public void draw(Frame frame, int mouseX, int mouseY, float partialTicks)
    {
        if(frame.isDragging())
        {
            frame.setX(mouseX - frame.getDragX());
            frame.setY(mouseY - frame.getDragY());
            frame.setPrefX(frame.getX());
            frame.setPrefY(frame.getY());
            frame.layoutComponents();
        }

        final float x = frame.getX() + frame.getMargin(Direction.LEFT),
                    y = frame.getY() + frame.getMargin(Direction.TOP);
        RenderUtils.drawRect(
                frame.getX(),
                frame.getY() - draggableHeight,
                frame.getFullWidth(),
                draggableHeight,
                draggableColor);
        RenderUtils.drawRect(
                x,
                y,
                frame.getFullWidth(),
                frame.getFullHeight(),
                backgroundColor);
        RenderUtils.drawBorder(
                x - borderSize * 0.3125f,
                y - draggableHeight - borderSize * 0.3125f,
                frame.getFullWidth() + borderSize * 0.3125f,
                frame.getFullHeight() + draggableHeight + borderSize * 0.3125f,
                borderSize * 0.3125f,
                borderColor);
        if(frame.getTitle() != null)
            fontRenderer.drawString(
                    frame.getTitle(),
                    x + 2,
                    y - draggableHeight / 2 - fontRenderer.getStringHeight(frame.getTitle()) / 2,
                    fontColor);

        glEnable(GL_SCISSOR_TEST);
        RenderUtils.prepareScissorBox(
                x + borderSize * 0.3125f,
                y + borderSize * 0.3125f,
                x + frame.getFullWidth() - borderSize * 0.3125f,
                y + frame.getFullHeight() - borderSize * 0.3125f);
        super.draw(frame, mouseX, mouseY, partialTicks);
        glDisable(GL_SCISSOR_TEST);
    }

    @Override
    public String getStyle()
    {
        return ".vis-frame {\n" +
               "   -background-color: #ff252525;\n" +
               "   -draggable-color: #ff1f1f1f;\n" +
               "   -border-size: 1;\n" +
               "   -border-color: #ff0074A1;\n" +
               "   -draggable-height: 8;\n" +
               "   -font-color: #ffffffff;\n" +
               "   -font-family: 'arial';\n" +
               "   -font-size: 16;\n" +
               "}\n";
    }
}
