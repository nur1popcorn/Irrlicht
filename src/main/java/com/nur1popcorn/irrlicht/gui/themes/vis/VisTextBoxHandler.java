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

import com.nur1popcorn.irrlicht.gui.components.TextBox;
import com.nur1popcorn.irrlicht.gui.components.layout.Direction;
import com.nur1popcorn.irrlicht.gui.font.FontRenderer;
import com.nur1popcorn.irrlicht.gui.themes.ComponentHandler;
import com.nur1popcorn.irrlicht.gui.themes.MouseEventType;
import com.nur1popcorn.irrlicht.gui.themes.Theme;
import com.nur1popcorn.irrlicht.gui.themes.parsing.Style;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.ColorValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.NumberValue;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.StringValue;
import com.nur1popcorn.irrlicht.management.keybind.KeyMask;
import com.nur1popcorn.irrlicht.utils.RenderUtils;
import com.nur1popcorn.irrlicht.management.TimeHelper;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * The {@link VisTextBoxHandler} is a simple {@link TextBox} {@link ComponentHandler}.
 *
 * @see TextBox
 * @see ComponentHandler
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class VisTextBoxHandler extends ComponentHandler<TextBox>
{
    private TimeHelper timer = new TimeHelper();
    private FontRenderer fontRenderer;
    private TextBox lastClicked;
    private int backgroundColor,
                borderSize,
                borderColor,
                errorColor,
                hoverColor,
                cursorColor,
                selectionColor,
                fontColor;

    public VisTextBoxHandler(Theme theme)
    {
        super(theme);
    }

    @Override
    public void init()
    {
        final Style style = getStyle("vis-textbox");
        backgroundColor = ((ColorValue)style.getValue("background-color")).value;
        errorColor = ((ColorValue)style.getValue("error-color")).value;
        hoverColor = ((ColorValue)style.getValue("hover-color")).value;
        cursorColor = ((ColorValue)style.getValue("cursor-color")).value;
        selectionColor = ((ColorValue)style.getValue("selection-color")).value;
        borderSize = ((NumberValue)style.getValue("border-size")).value.intValue();
        borderColor = ((ColorValue)style.getValue("border-color")).value;
        fontColor = ((ColorValue)style.getValue("font-color")).value;
        fontRenderer = theme.getFontRenderer(new Font(((StringValue)style.getValue("font-family")).value, Font.PLAIN, ((NumberValue)style.getValue("font-size")).value.intValue()));
    }

    @Override
    public boolean handleInput(TextBox textBox, int mouseX, int mouseY, int mouseButton, MouseEventType mouseEventType)
    {
        final float x = textBox.getX() + textBox.getMargin(Direction.LEFT),
                    y = textBox.getY() + textBox.getMargin(Direction.TOP);
        if(mouseX >= x &&
           mouseY >= y &&
           mouseX <= x + textBox.getFullWidth() &&
           mouseY <= y + textBox.getFullHeight())
        {
            theme.getGuiManager().focus(textBox);
            lastClicked = textBox;
            return true;
        }
        return false;
    }

    @Override
    public boolean handleKeyTyped(TextBox textBox, char typedChar, int keyCode)
    {
        if(lastClicked == textBox && theme.getGuiManager().isFocused(textBox))
        {
            if(KeyMask.SHIFT.isDown())
                switch(keyCode)
                {
                    case Keyboard.KEY_END:
                        textBox.setSelectionPosition(textBox.getText().length());
                        return true;
                    case Keyboard.KEY_LEFT:
                        if(textBox.getSelectionPosition() - 1 >= 0)
                            textBox.setSelectionPosition(textBox.getSelectionPosition() - 1);
                        return true;
                    case Keyboard.KEY_RIGHT:
                        if(textBox.getSelectionPosition() + 1 <= textBox.getText().length())
                            textBox.setSelectionPosition(textBox.getSelectionPosition() + 1);
                        return true;
                }
            else if(KeyMask.CONTROL.isDown())
                switch(keyCode)
                {
                    case Keyboard.KEY_A:
                        textBox.setCursorPosition(0);
                        textBox.setScrollOffset(0);
                        textBox.setSelectionPosition(textBox.getText().length());
                        return true;
                    case Keyboard.KEY_C:
                        if(!textBox.isPassword())
                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(textBox.getText().substring(textBox.getCursorPosition(), textBox.getSelectionPosition())), null);
                        return true;
                    case Keyboard.KEY_X:
                        if(!textBox.isPassword())
                        {
                            final int pos[] = textBox.getCursorPosition() < textBox.getSelectionPosition() ?
                                  new int[] { textBox.getCursorPosition(), textBox.getSelectionPosition() } :
                                  new int[] { textBox.getSelectionPosition(), textBox.getCursorPosition() };
                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                                    new StringSelection(textBox.getText().substring(pos[0], pos[1])), null);
                            textBox.setText(
                                    new StringBuilder(textBox.getText())
                                            .replace(pos[0], pos[1], "")
                                            .toString());
                            textBox.setCursorPosition(pos[0]);
                            textBox.setSelectionPosition(pos[0]);
                        }
                        return true;
                    case Keyboard.KEY_V:
                        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
                        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
                            try
                            {
                                final String text = (String)transferable.getTransferData(DataFlavor.stringFlavor);
                                if(textBox.getCursorPosition() != textBox.getSelectionPosition())
                                {
                                    final int pos[] = textBox.getCursorPosition() < textBox.getSelectionPosition() ?
                                          new int[] { textBox.getCursorPosition(), textBox.getSelectionPosition() } :
                                          new int[] { textBox.getSelectionPosition(), textBox.getCursorPosition() };
                                    String s = "";
                                    for(int i = 0; i < textBox.getText().length(); i++)
                                        s += textBox.getScrollOffset() == i | textBox.getCursorPosition() == i ? "|" + (textBox.getScrollOffset() == i ? "*" : "") : "-";
                                    System.out.println(s);
                                    System.out.println(textBox.getScrollOffset() + ", " + (text.length() - (pos[1] - pos[0])) + ", " + textBox.getScrollOffset() + (text.length() - (pos[1] - pos[0])));
                                    textBox.setText(
                                            new StringBuilder(textBox.getText())
                                                    .replace(pos[0], pos[1], text)
                                                    .toString());
                                    textBox.setCursorPosition(pos[0] + text.length());
                                    textBox.setScrollOffset(textBox.getScrollOffset() + (text.length() - (pos[1] - pos[0])));
                                }
                                else
                                {
                                    textBox.setText(
                                            new StringBuilder(textBox.getText())
                                                    .insert(textBox.getCursorPosition(), text)
                                                    .toString());
                                    textBox.setCursorPosition(textBox.getCursorPosition() + text.length());
                                    textBox.setScrollOffset(textBox.getScrollOffset() + text.length());
                                }
                                textBox.setSelectionPosition(textBox.getCursorPosition());
                            }
                            catch (UnsupportedFlavorException | IOException e)
                            {
                                e.printStackTrace();
                            }
                        return true;
                }
            switch(keyCode)
            {
                case Keyboard.KEY_LEFT:
                    if(textBox.getCursorPosition() - 1 >= 0)
                    {
                        textBox.setCursorPosition(textBox.getCursorPosition() - 1);
                        textBox.setSelectionPosition(textBox.getCursorPosition());
                        if(textBox.getCursorPosition() < textBox.getScrollOffset() + 2)
                            textBox.setScrollOffset(textBox.getCursorPosition() - 1 > 0 ? textBox.getCursorPosition() - 1 : 0);
                    }
                    return true;
                case Keyboard.KEY_RIGHT:
                    if(textBox.getCursorPosition() + 1 <= textBox.getText().length())
                    {
                        textBox.setCursorPosition(textBox.getCursorPosition() + 1);
                        textBox.setSelectionPosition(textBox.getCursorPosition());
                        scrollRight(textBox);
                    }
                    return true;
                case Keyboard.KEY_BACK:
                    if(textBox.getText().length() > 0 &&
                      (textBox.getCursorPosition() != 0 | textBox.getCursorPosition() != textBox.getSelectionPosition()))
                    {
                        final int pos[] = textBox.getCursorPosition() != textBox.getSelectionPosition() ?
                                          textBox.getCursorPosition() < textBox.getSelectionPosition() ?
                              new int[] { textBox.getCursorPosition(), textBox.getSelectionPosition() } :
                              new int[] { textBox.getSelectionPosition(), textBox.getCursorPosition() } :
                              new int[] { textBox.getCursorPosition() - 1, textBox.getCursorPosition() };
                        textBox.setText(
                                new StringBuilder(textBox.getText())
                                        .replace(pos[0], pos[1], "")
                                        .toString());
                        textBox.setCursorPosition(pos[0]);
                        textBox.setSelectionPosition(textBox.getCursorPosition());

                        if(textBox.getScrollOffset() != 0 && pos[0] - textBox.getScrollOffset() == 0)
                            textBox.setScrollOffset(textBox.getScrollOffset() - 1);
                    }
                    return true;
            }

            if(typedChar >= 32)
            {
                textBox.setText(
                        new StringBuilder(textBox.getText())
                                .insert(textBox.getCursorPosition(), typedChar)
                                .toString());
                textBox.setCursorPosition(textBox.getCursorPosition() + 1);
                textBox.setSelectionPosition(textBox.getCursorPosition());
                scrollRight(textBox);
            }
            return true;
        }
        return false;
    }

    private void scrollRight(TextBox textBox)
    {
        final String text = fontRenderer.trimToWidth(
                (textBox.isPassword() ? textBox.getText().replaceAll(".", "*") : textBox.getText()).substring(textBox.getScrollOffset()),
                textBox.getFullWidth());
        if(textBox.getCursorPosition() - textBox.getScrollOffset() > text.length())
        {
            textBox.setScrollOffset(textBox.getScrollOffset() + 1);
            scrollRight(textBox);
        }
    }

    @Override
    public void draw(TextBox textBox, int mouseX, int mouseY, float partialTicks)
    {
        final float x = textBox.getX() + textBox.getMargin(Direction.LEFT),
                    y = textBox.getY() + textBox.getMargin(Direction.TOP);
        RenderUtils.drawRect(
                x,
                y,
                textBox.getFullWidth(),
                textBox.getFullHeight(),
                mouseX >= x &&
                mouseY >= y &&
                mouseX <= x + textBox.getFullWidth() &&
                mouseY <= y + textBox.getFullHeight() ? hoverColor : backgroundColor);
        boolean flag;
        if(lastClicked == textBox | (flag = (textBox.shouldVerifyInput() && textBox.hasError())))
            RenderUtils.drawBorder(
                    x,
                    y,
                    textBox.getFullWidth(),
                    textBox.getFullHeight(),
                    borderSize * 0.3125f,
                    flag ? errorColor : borderColor);
        final String text = fontRenderer.trimToWidth(
                (textBox.isPassword() ? textBox.getText().replaceAll(".", "*") : textBox.getText()).substring(textBox.getScrollOffset()),
                textBox.getFullWidth());
        fontRenderer.drawString(text, x, y + getPositonOffset(textBox, new float[] { fontRenderer.getMaxHeight(), 0 })[0], fontColor);

        if(theme.getGuiManager().isFocused(textBox))
        {
            RenderUtils.drawRect(
                    textBox.getX() + textBox.getMargin(Direction.LEFT) + fontRenderer.getStringWidth(text.substring(0, textBox.getCursorPosition() - textBox.getScrollOffset())),
                    textBox.getY() + textBox.getMargin(Direction.TOP) + 1,
                    0.3125f,
                    textBox.getFullHeight() - 2,
                    ((int)((cursorColor >> 24 & 255) * (Math.sin(timer.getMSPassed() / 125) + 1) / 2) << 24) +
                           (cursorColor >> 16 & 255 << 16) +
                           (cursorColor >> 8 & 255 << 8) +
                           (cursorColor & 255));
            final int pos[] = textBox.getCursorPosition() < textBox.getSelectionPosition() ?
                  new int[] { textBox.getCursorPosition(), textBox.getSelectionPosition() } :
                  new int[] { textBox.getSelectionPosition(), textBox.getCursorPosition() };
            RenderUtils.drawRect(
                    textBox.getX() + textBox.getMargin(Direction.LEFT) + fontRenderer.getStringWidth(textBox.getText().substring(0, pos[0])),
                    textBox.getY() + textBox.getMargin(Direction.TOP) + 1,
                    fontRenderer.getStringWidth(textBox.getText().substring(pos[0], pos[1])),
                    textBox.getFullHeight() - 2,
                    selectionColor);
        }
    }

    @Override
    public String getStyle()
    {
        return ".vis-textbox {\n" +
               "   -background-color: #ff333333;\n" +
               "   -border-size: 1;\n" +
               "   -border-color: #ff006496;\n" +
               "   -error-color: #ffff0000;\n" +
               "   -hover-color: #ff3e3e42;\n" +
               "   -cursor-color: #ffd1d1d1;\n" +
               "   -selection-color: #32ffffff;\n" +
               "   -font-family: 'arial';\n" +
               "   -font-size: 16;\n" +
               "   -font-color: #ffffffff;\n" +
               "}\n";
    }
}
