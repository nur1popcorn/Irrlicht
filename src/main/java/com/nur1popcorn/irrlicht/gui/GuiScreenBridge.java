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

package com.nur1popcorn.irrlicht.gui;

import com.nur1popcorn.irrlicht.Irrlicht;
import com.nur1popcorn.irrlicht.engine.mapper.WrapperBridge;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;
import com.nur1popcorn.irrlicht.engine.wrappers.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * The {@link GuiScreenBridge} is a class used to emulate {@link GuiScreen}s.
 *
 * @see WrapperBridge
 * @see GuiScreen
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
public class GuiScreenBridge extends WrapperBridge implements GuiScreen
{
    protected int width,
                  height;

    public GuiScreenBridge()
    {
        super(GuiScreen.class);
    }

    @Override
    public final void handleInput()
    {
        if(Mouse.isCreated())
            while(Mouse.next())
            {
                final int x = Mouse.getEventX() * width / Display.getWidth(),
                          y = height - Mouse.getEventY() * height / Display.getHeight() - 1,
                          button = Mouse.getEventButton();
                if(Mouse.getEventButtonState())
                    mouseClicked(x, y, button);
                else if(button != -1)
                    mouseReleased(x, y, button);
            }

        if(Keyboard.isCreated())
            while(Keyboard.next())
                if(Keyboard.getEventKeyState())
                    keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
    }

    /**
     * Called when any mouse button is clicked.
     *
     * @param mouseX the x position of the mouse.
     * @param mouseY the y position of the mouse.
     * @param mouseButton the mouse button clicked.
     *
     * @see GuiScreen
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {}

    /**
     * Called when any mouse button is released.
     *
     * @param mouseX the x position of the mouse.
     * @param mouseY the y position of the mouse.
     * @param mouseButton the mouse button released.
     *
     * @see GuiScreen
     */
    public void mouseReleased(int mouseX, int mouseY, int mouseButton)
    {}

    /**
     * Called when any key is pressed.
     *
     * @param typedChar the character which was typed.
     * @param keyCode the keycode of the key which was pressed.
     *
     * @see GuiScreen
     */
    public void keyTyped(char typedChar, int keyCode)
    {
        if(keyCode == Keyboard.KEY_ESCAPE)
            Irrlicht.getMinecraft()
                    .displayGuiScreen(null);
    }

    @Override
    public void initGui(Minecraft minecraft, int width, int height)
    {
        Keyboard.enableRepeatEvents(true);
        this.width = width;
        this.height = height;
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height)
    {
        initGui(minecraft, width, height);
    }

    @Override
    public boolean shouldPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {}

    @Override
    public void onUpdate()
    {}

    @Override
    public void onClose()
    {}
}
