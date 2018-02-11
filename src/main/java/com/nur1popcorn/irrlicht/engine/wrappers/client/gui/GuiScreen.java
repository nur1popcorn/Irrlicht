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

package com.nur1popcorn.irrlicht.engine.wrappers.client.gui;

import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;

/**
 * The {@link GuiScreen} is a class used to display user interfaces.
 *
 * @see Wrapper
 * @see Minecraft
 * @see Minecraft#displayGuiScreen(GuiScreen)
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.STRING_CONST,
                 declaring = Minecraft.class,
                 constants = { "Invalid Item!" })
public interface GuiScreen extends Wrapper
{

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIRST_MATCH | Mapper.OPCODES,
                     opcodes = {
                        Opcodes.ALOAD,
                        Opcodes.ALOAD,
                        Opcodes.PUTFIELD
                     })
    public void initGui(Minecraft minecraft, int width, int height);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIRST_MATCH | Mapper.OPCODES,
                     opcodes = {
                        Opcodes.ALOAD,
                        Opcodes.ALOAD,
                        Opcodes.ILOAD,
                        Opcodes.ILOAD,
                        Opcodes.INVOKEVIRTUAL
                     })
    public void resize(Minecraft minecraft, int width, int height);

    public void drawScreen(int mouseX, int mouseY, float partialTicks);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIRST_MATCH | Mapper.OPCODES,
                     opcodes = {
                        Opcodes.ALOAD,
                        Opcodes.INVOKEVIRTUAL,
                        Opcodes.GOTO
                     })
    public void handleInput();

    @DiscoveryMethod(checks = Mapper.CUSTOM)
    public void onUpdate();

    @DiscoveryMethod(checks = Mapper.CUSTOM)
    public void onClose();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIRST_MATCH | Mapper.OPCODES,
                     opcodes = {
                        Opcodes.ICONST_1,
                        Opcodes.IRETURN
                     })
    public boolean shouldPauseGame();
}
