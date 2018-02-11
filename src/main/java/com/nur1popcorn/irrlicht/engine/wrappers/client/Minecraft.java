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

package com.nur1popcorn.irrlicht.engine.wrappers.client;

import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.entity.PlayerSp;
import com.nur1popcorn.irrlicht.engine.wrappers.client.gui.GuiIngame;
import com.nur1popcorn.irrlicht.engine.wrappers.client.gui.GuiScreen;
import com.nur1popcorn.irrlicht.engine.wrappers.client.minecraft.Timer;
import com.nur1popcorn.irrlicht.engine.wrappers.client.renderer.EntityRenderer;
import com.nur1popcorn.irrlicht.engine.wrappers.client.settings.GameSettings;
import com.nur1popcorn.irrlicht.engine.wrappers.world.WorldClient;

import java.lang.reflect.Modifier;

/**
 * The {@link Minecraft} class is storing most of the useful variables it's the main
 * of the game.
 *
 * @see Wrapper
 * @see PlayerSp
 * @see Timer
 * @see GameSettings
 * @see GuiIngame
 * @see GuiScreen
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@DiscoveryMethod(checks = Mapper.CUSTOM)
public interface Minecraft extends Wrapper
{
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public PlayerSp getPlayer();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public Timer getTimer();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public Timer setTimer(Timer timer);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public GameSettings getGameSettings();

    @DiscoveryMethod(checks = Mapper.CUSTOM | Mapper.FIELD)
    public int getLeftClickDelay();

    @DiscoveryMethod(checks = Mapper.CUSTOM | Mapper.FIELD)
    public void setLeftClickDelay(int delay);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public GuiIngame getIngameGui();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public GuiScreen getGuiScreen();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public WorldClient getWorld();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public EntityRenderer getEntityRenderer();

    @DiscoveryMethod(modifiers = Modifier.PUBLIC | Modifier.STATIC)
    public Minecraft getMinecraft();

    public void displayGuiScreen(GuiScreen guiScreen);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.STRING_CONST,
                     constants = "Null returned as \'hitResult\', this shouldn't happen!")
    public void clickMouse();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.STRING_CONST,
                     constants = "Ticking screen")
    public void tick();
}
