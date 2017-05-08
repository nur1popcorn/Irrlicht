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

package com.nur1popcorn.irrlicht.gui.components.containers;

import com.nur1popcorn.irrlicht.gui.components.Component;
import com.nur1popcorn.irrlicht.gui.components.layout.Align;
import com.nur1popcorn.irrlicht.gui.components.layout.Direction;
import com.nur1popcorn.irrlicht.gui.components.layout.Orientation;

/**
 * The {@link BoxContainer}
 *
 * @see Container
 * @see Component
 * @see Align
 * @see Direction
 * @see Orientation
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class BoxContainer extends Container
{
    private Orientation orientation;

    public BoxContainer(Orientation orientation)
    {
        this.orientation = orientation;
    }

    public BoxContainer(Orientation orientation, Component... components)
    {
        super(components);
        this.orientation = orientation;
    }

    /**
     * @param orientation the {@link Orientation} used to distinguish between vertical
     *                    and horizontal size.
     * @param align the {@link Align} for which the base offset should be calculated
     *              like this:
     *        |-------|--------|-------|
     *        |       |        |       |
     *        | LEFT  | CENTER | RIGHT | } LEFT
     *        |       |        |       |
     *        |-------|--------|-------|
     *        |       |        |       |
     *        | LEFT  | CENTER | RIGHT | } CENTER
     *        |       |        |       |
     *        |-------|--------|-------|
     *        |       |        |       |
     *        | LEFT  | CENTER | RIGHT | } RIGHT
     *        |       |        |       |
     *        |-------|--------|-------|
     *
     * @param requiredSize the total width of the area that needs to be aligned.
     *
     * @see #layoutComponents()
     */
    private float getPositionOffset(Orientation orientation, Align align, float requiredSize)
    {
        switch(align)
        {
            default:
            case LEFT:
                return 0;
            case CENTER:
                return (getSize(orientation) - requiredSize) / 2;
            case RIGHT:
                return getSize(orientation) - requiredSize;
        }
    }

    /**
     * @param orientation the {@link Orientation} which the opposite should be returned
     *                    of.
     *
     * @see #layoutComponents()
     *
     * @return the opposite of the given orientation.
     */
    private Orientation getOpposite(Orientation orientation)
    {
        return orientation == Orientation.VERTICAL ? Orientation.HORIZONTAL : Orientation.VERTICAL;
    }

    /**
     * @param orientation the {@link Orientation} used to determine whether to return
     *                    the width or height.
     *
     * @see #layoutComponents()
     *
     * @return the width / height of this {@link Container} based on the {@link Orientation} provided.
     */
    private float getSize(Orientation orientation)
    {
        return orientation == Orientation.VERTICAL ? height : width;
    }

    /**
     * @param orientation the {@link Orientation} used to determine whether to return
     *                    the width or height.
     * @param component the {@link Component} for which the action should be performed.
     *
     * @see #layoutComponents()
     *
     * @return the full width / height needed by a {@link Component} provided based on the {@link Orientation}.
     */
    private float getFullSizeNeeded(Orientation orientation, Component component)
    {
        return orientation == Orientation.VERTICAL ? component.getFullHeightNeeded() : component.getFullWidthNeeded();
    }

    /**
     * @param orientation the {@link Orientation} used to determine whether to return
     *                    the width or height.
     * @param component the {@link Component} for which the action should be performed.
     *
     * @see #layoutComponents()
     *
     * @return the preferred width / height by a {@link Component} provided based
     *         on the {@link Orientation}.
     */
    private float getPrefSize(Orientation orientation, Component component)
    {
        return orientation == Orientation.VERTICAL ? component.getPrefHeight() : component.getPrefWidth();
    }

    /**
     * @param orientation the {@link Orientation} used to determine whether to return
     *                    the width or height.
     * @param component the {@link Component} for which the action should be performed.
     *
     * @see #layoutComponents()
     *
     * @return the the full preferred width / height needed by a {@link Component}
     *         provided based on the {@link Orientation}.
     */
    private float getFullPrefSizeNeeded(Orientation orientation, Component component)
    {
        return orientation == Orientation.VERTICAL ? component.getFullPrefHeightNeeded() : component.getFullPrefWidthNeeded();
    }

    /**
     * @param orientation the {@link Orientation} used to determine whether to use the
     *                    width or height.
     * @param component the {@link Component} for which the action should be performed.
     *
     * @see #layoutComponents()
     *
     * @return whether or not the {@link Component} is fitting it parent based on the
     *         {@link Orientation} provided.
     */
    private boolean isFittingSize(Orientation orientation, Component component)
    {
        return orientation == Orientation.VERTICAL ? component.isFittingHeight() : component.isFittingWidth();
    }

    /**
     * Sets the width / height for given component based on the {@link Orientation} and
     * size given.
     *
     * @param orientation the {@link Orientation} used to determine whether to set the
     *                    width or height.
     * @param component the {@link Component} for which the action should be performed.
     *
     * @see #layoutComponents()
     */
    private void setSize(Orientation orientation, Component component, float size)
    {
        if(orientation == Orientation.VERTICAL)
            component.setHeight(size - component.getMargin(Direction.TOP) - component.getPadding(Direction.TOP));
        else
            component.setWidth(size - component.getMargin(Direction.LEFT) - component.getPadding(Direction.LEFT));
    }

    /**
     * @return the {@link Container}'s {@link Orientation}.
     */
    public Orientation getOrientation()
    {
        return orientation;
    }

    @Override
    public void layoutComponents()
    {
        //determine size needed.
        float fullSizeNeeded = 0,
              fitCount = 0;
        for(Component component : components)
        {
            fullSizeNeeded += getFullPrefSizeNeeded(orientation, component);
            if(isFittingSize(orientation, component))
            {
                fitCount++;
                fullSizeNeeded -= getPrefSize(orientation, component);
            }
        }

        //add additional space if available.
        if(getSize(orientation) - fullSizeNeeded > 0 && fitCount > 0)
            for(Component component : components)
                if(isFittingSize(orientation, component))
                    setSize(orientation, component, (getSize(orientation) - fullSizeNeeded) / fitCount);

        //set size for remaining components.
        final Orientation opposite = getOpposite(orientation);
        for(Component component : components)
        {
            if(isFittingSize(opposite, component))
                setSize(opposite, component, getSize(opposite));
            else
                setSize(opposite, component, getPrefSize(opposite, component));

            if(!isFittingSize(orientation, component))
                setSize(orientation, component, getPrefSize(orientation, component));
        }

        //determine final position and layout containers.
        float offset = orientation == Orientation.VERTICAL ?
                y + getMargin(Direction.TOP) + getPadding(Direction.TOP) :
                x + getMargin(Direction.LEFT) + getPadding(Direction.LEFT);
        for(Component component : components)
        {
            if(orientation == Orientation.VERTICAL)
            {
                component.setX(x + getMargin(Direction.LEFT) + getPadding(Direction.LEFT) + getPositionOffset(Orientation.HORIZONTAL, align[Orientation.HORIZONTAL.ordinal()], component.getFullWidthNeeded()));
                component.setY(offset);
                offset += component.getFullHeightNeeded();
            }
            else
            {
                component.setX(offset + getPositionOffset(orientation, align[1], fullSizeNeeded));
                component.setY(y + getMargin(Direction.TOP) + getPadding(Direction.TOP) + getPositionOffset(Orientation.VERTICAL, align[Orientation.VERTICAL.ordinal()], component.getFullHeightNeeded()));
                offset += getFullSizeNeeded(orientation, component);
            }

            if(component instanceof Container)
                ((Container) component).layoutComponents();
        }

        float maxX = 0,
              maxY = 0;
        for(Component component : components)
        {
            maxX = Math.max(component.getX() + component.getFullWidthNeeded() - getX(), maxX);
            maxY = Math.max(component.getY() + component.getFullHeightNeeded() - getY(), maxY);
        }
        width = maxX;
        height = maxY;
    }
}
