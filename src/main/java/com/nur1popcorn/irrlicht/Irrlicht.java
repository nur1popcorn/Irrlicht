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

package com.nur1popcorn.irrlicht;

import com.nur1popcorn.irrlicht.engine.mapper.WrapperDelegationHandler;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;
import com.nur1popcorn.irrlicht.gui.GuiManager;
import com.nur1popcorn.irrlicht.modules.ModuleManager;

/**
 * The {@link Irrlicht} is the main class of the client.
 *
 * @see ModuleManager
 * @see GuiManager
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Irrlicht
{
    /**
     * The name of the client.
     */
    public static final String NAME = "Irrlicht",
    /**
     * The client's version.
     */
                               VERSION = "1.0.0-alpha";

    /**
     * An array containing all of the authors names.
     */
    public static final String AUTHORS[] =
    {
        "nur1popcorn"
    };

    private static Irrlicht instance;
    private ModuleManager moduleManager;
    private GuiManager guiManager;

    private Irrlicht()
    {
        instance = this;
        moduleManager = ModuleManager.createModuleManager();
        guiManager = GuiManager.createGuiManager();
    }

    /**
     * Creates an instance of the client.
     */
    public static void bootstrap()
    {
        new Irrlicht();
    }

    /**
     * @return an instance of the {@link Irrlicht} class;
     */
    public static Irrlicht getIrrlicht()
    {
        return instance;
    }

    /**
     * @see WrapperDelegationHandler
     * @see Minecraft
     *
     * @return an instance of the {@link Minecraft} class.
     */
    public static Minecraft getMinecraft()
    {
        return WrapperDelegationHandler.createWrapperProxy(Minecraft.class, null)
                .getMinecraft();
    }

    /**
     * @see ModuleManager
     *
     * @return an instance of the {@link ModuleManager}
     */
    public ModuleManager getModuleManager()
    {
        return moduleManager;
    }

    /**
     * @see GuiManager
     *
     * @return an instance of the {@link GuiManager}
     */
    public GuiManager getGuiManager()
    {
        return guiManager;
    }
}
