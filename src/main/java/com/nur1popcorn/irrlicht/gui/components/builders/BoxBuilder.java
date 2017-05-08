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

package com.nur1popcorn.irrlicht.gui.components.builders;

import com.nur1popcorn.irrlicht.gui.components.Component;
import com.nur1popcorn.irrlicht.gui.components.containers.BoxContainer;
import com.nur1popcorn.irrlicht.gui.components.layout.Align;
import com.nur1popcorn.irrlicht.gui.components.layout.Direction;
import com.nur1popcorn.irrlicht.gui.components.layout.Orientation;

/**
 * The {@link BoxBuilder}
 *
 * @see BoxContainer
 * @see Orientation
 * @see Direction
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class BoxBuilder
{
    private BoxContainer finalBoxContainer = new BoxContainer(Orientation.VERTICAL),
                         currentBoxContainer = new BoxContainer(Orientation.HORIZONTAL);

    /**
     * Adds {@link Component}s to the current horizontal {@link BoxContainer}.
     * 
     * @param components the {@link Component}s which will be added to the current
     *                   horizontal {@link BoxContainer}.
     *
     * @return the current {@link BoxBuilder}.
     */
    public BoxBuilder append(Component... components)
    {
        currentBoxContainer.add(components);
        for(Component component : components)
        {
            float size = component.getFullPrefHeightNeeded();
            if(size > currentBoxContainer.getPrefHeight())
                currentBoxContainer.setPrefHeight(size);
        }
        return this;
    }

    /**
     * Will add the current horizontal {@link BoxContainer} to the final vertical
     * {@link BoxContainer}.
     * 
     * @return the current {@link BoxBuilder}.
     */
    public BoxBuilder row()
    {
        currentBoxContainer.setFitWidth(true);
        finalBoxContainer.add(currentBoxContainer);
        currentBoxContainer = new BoxContainer(Orientation.HORIZONTAL);
        return this;
    }

    /**
     * Sets the margin of the current horizontal {@link BoxContainer}.
     * 
     * @param direction the {@link Direction} used to determine where to set the margin.
     * @param margin the margin set for given {@link Direction}.
     *
     * @return the current {@link BoxBuilder}.
     */
    public BoxBuilder setMargin(Direction direction, int margin)
    {
        currentBoxContainer.setMargin(direction, margin);
        return this;
    }

    /**
     * Sets the padding of the current horizontal {@link BoxContainer}.
     *
     * @param direction the {@link Direction} used to determine where to set the padding.
     * @param padding the padding set for given {@link Direction}.
     *
     * @return the current {@link BoxBuilder}.
     */
    public BoxBuilder setPadding(Direction direction, int padding)
    {
        currentBoxContainer.setPadding(direction, padding);
        return this;
    }

    /**
     * Sets the align of the current horizontal {@link BoxContainer}.
     *
     * @param vAlign the vertical {@link Align} used to align the {@link Component}.
     * @param hAlign the horizontal {@link Align} used to align the {@link Component}.
     *
     * @return the current {@link BoxBuilder}.
     */
    public BoxBuilder setAlign(Align vAlign, Align hAlign)
    {
        currentBoxContainer.setAlign(vAlign, hAlign);
        return this;
    }

    /**
     * Resets the {@link BoxBuilder} to default.
     *
     * @return the current {@link BoxBuilder}.
     */
    public BoxBuilder clear()
    {
        finalBoxContainer = new BoxContainer(Orientation.VERTICAL);
        currentBoxContainer = new BoxContainer(Orientation.HORIZONTAL);
        return this;
    }

    /**
     * @return the final vertical {@link BoxContainer}.
     */
    public BoxContainer build()
    {
        finalBoxContainer.setFitWidth(true);
        finalBoxContainer.setFitHeight(true);
        if(!(currentBoxContainer.getComponents().isEmpty()))
            row();
        return finalBoxContainer;
    }
}
