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

import com.nur1popcorn.irrlicht.engine.hooker.Hooker;
import com.nur1popcorn.irrlicht.engine.hooker.HookingMethod;
import com.nur1popcorn.irrlicht.engine.hooker.impl.Render2DEvent;
import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;

/**
 * The {@link GuiIngame} is a class used to render the in game gui like the toolbar.
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
                 constants = { "textures/misc/vignette.png", "textures/gui/widgets.png" })
public interface GuiIngame extends Wrapper
{
    @HookingMethod(value = Render2DEvent.class,
                   flags = Hooker.DEFAULT | Hooker.AFTER)
    public void renderWidgets(float partialTicks);
}
