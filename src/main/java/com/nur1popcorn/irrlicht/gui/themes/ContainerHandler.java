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

package com.nur1popcorn.irrlicht.gui.themes;

import com.nur1popcorn.irrlicht.gui.components.Component;
import com.nur1popcorn.irrlicht.gui.components.containers.Container;

/**
 * The {@link ContainerHandler} is a default {@link ComponentHandler} for {@link Container}s.
 *
 * @see Theme
 * @see Component
 * @see Container
 * @see ComponentHandler
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class ContainerHandler<T extends Container> extends ComponentHandler<T>
{
    public ContainerHandler(Theme theme)
    {
        super(theme);
    }

    @Override
    public boolean handleInput(T container, int mouseX, int mouseY, int mouseButton, MouseEventType mouseEventType)
    {
        for(Component component : container.getComponents())
            if(theme.handleInput(component, mouseX, mouseY, mouseButton, mouseEventType))
                return true;
        return false;
    }

    @Override
    public boolean handleKeyTyped(T container, char typedChar, int keyCode)
    {
        for(Component component : container.getComponents())
            if(theme.handleKeyTyped(component, typedChar, keyCode))
                return true;
        return false;
    }

    @Override
    public void draw(T container, int mouseX, int mouseY, float partialTicks)
    {
        container.getComponents()
                 .forEach(component -> theme.draw(component, mouseX, mouseY, partialTicks));
    }

    @Override
    public void update(T container)
    {
        container.getComponents()
                 .forEach(theme::update);
    }
}
