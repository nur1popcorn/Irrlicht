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

package com.nur1popcorn.irrlicht.gui.components;

import com.nur1popcorn.irrlicht.gui.components.containers.Container;
import com.nur1popcorn.irrlicht.gui.components.layout.Align;
import com.nur1popcorn.irrlicht.gui.components.layout.Direction;
import com.nur1popcorn.irrlicht.gui.components.layout.Orientation;

/**
 * The {@link Component} is the base class of all {@link Component}s and stores
 * some very important information used to display and position each and everyone.
 *
 * @see Orientation
 * @see Direction
 * @see Align
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public abstract class Component
{
    protected float x,
                    y,
                    prefX,
                    prefY,
                    width,
                    height,
                    prefWidth,
                    prefHeight,
                    padding[] = new float[4],
                    margin[] = new float[4];

    protected boolean fitWidth,
                      fitHeight;

    protected Align align[] = new Align[]
    {
        Align.LEFT,
        Align.LEFT
    };

    protected Container container;

    public Component()
    {}

    public Component(float width, float height)
    {
        prefWidth = width;
        prefHeight = height;
    }

    public Component(float x, float y, float width, float height)
    {
        this(width, height);
        prefX = x;
        prefY = y;
    }

    /**
     * Sets the x position.
     * 
     * @param x the x position.
     */
    public void setX(float x)
    {
        this.x = x;
    }
    
    /**
     * @return the x position of the {@link Component}.
     */
    public float getX()
    {
        return x;
    }

    /**
     * @param y the y position.
     */
    public void setY(float y)
    {
        this.y = y;
    }

    /**
     * @return the y position of the {@link Component}.
     */
    public float getY()
    {
        return y;
    }

    /**
     * Sets prefX may or may not be ignored by the {@link Container}.
     *
     * @param prefX the prefX position.
     */
    public void setPrefX(float prefX)
    {
        this.prefX = prefX;
    }

    /**
     * @return the prefX position of the {@link Component}.
     */
    public float getPrefX()
    {
        return prefX;
    }

    /**
     * Sets prefY may or may not be ignored by the {@link Container}.
     *
     * @param prefY the prefY position.
     */
    public void setPrefY(float prefY)
    {
        this.prefY = prefY;
    }

    /**
     * @return the prefY position of the {@link Component}.
     */
    public float getPrefY()
    {
        return prefY;
    }

    /**
     * Sets width may or may not be ignored by the {@link Container}.
     *
     * @param width the width position.
     */
    public void setWidth(float width)
    {
        this.width = width;
    }

    /**
     * @return the width of the {@link Component}.
     */
    public float getWidth()
    {
        return width;
    }

    /**
     * Sets height may or may not be ignored by the {@link Container}.
     *
     * @param height the height position.
     */
    public void setHeight(float height)
    {
        this.height = height;
    }

    /**
     * @return the height of the {@link Component}.
     */
    public float getHeight()
    {
        return height;
    }

    /**
     * Sets prefWidth defines the preferred width for a {@link Component} which may
     * or may not be ignored by the {@link Container}.
     *
     * @param prefWidth the prefWidth position.
     */
    public void setPrefWidth(float prefWidth)
    {
        this.prefWidth = prefWidth;
    }

    /**
     * @return the preferred width of the {@link Component}.
     */
    public float getPrefWidth()
    {
        return prefWidth;
    }

    /**
     * Sets prefHeight defines the preferred height for a {@link Component} may
     * or may not be ignored by the {@link Container}.
     *
     * @param prefHeight the prefWidth position.
     */
    public void setPrefHeight(float prefHeight)
    {
        this.prefHeight = prefHeight;
    }

    /**
     * @return the preferred height of the {@link Component}.
     */
    public float getPrefHeight()
    {
        return prefHeight;
    }

    /**
     * Sets padding in {@link Direction}.
     *
     * @param direction the {@link Direction} for which the padding should be set.
     * @param padding the padding for the {@link Direction}.
     */
    public void setPadding(Direction direction, float padding)
    {
        this.padding[direction.ordinal()] = padding;
    }

    /**
     * @param direction the {@link Direction} for which the padding should be returned.
     *
     * @return the padding for the {@link Direction} provided.
     */
    public float getPadding(Direction direction)
    {
        return padding[direction.ordinal()];
    }

    /**
     * Sets margin at {@link Direction}.
     *
     * @param direction the {@link Direction} for which the margin should be set.
     * @param margin the margin for the direction.
     */
    public void setMargin(Direction direction, float margin)
    {
        this.margin[direction.ordinal()] = margin;
    }

    /**
     * @param direction the direction for which the margin should be returned.
     * @return the margin for the direction provided.
     */
    public float getMargin(Direction direction)
    {
        return margin[direction.ordinal()];
    }

    /**
     * Sets whether or not the {@link Component} is fitting it's parent width.
     *
     * @param fitWidth whether or not the {@link Component} is fitting it's parent width.
     */
    public void setFitWidth(boolean fitWidth)
    {
        this.fitWidth = fitWidth;
    }

    /**
     * @return whether or not the {@link Component} is fitting it's parent width.
     */
    public boolean isFittingWidth()
    {
        return fitWidth;
    }

    /**
     * Sets whether or not the {@link Component} is fitting it's parent height.
     *
     * @param fitHeight whether or not the {@link Component} is fitting it's parent height.
     */
    public void setFitHeight(boolean fitHeight)
    {
        this.fitHeight = fitHeight;
    }

    /**
     * @return whether or not the {@link Component} is fitting it's parent height.
     */
    public boolean isFittingHeight()
    {
        return fitHeight;
    }

    /**
     * Sets the {@link Orientation} {@link Align} for the {@link Component}.
     *
     * @param vAlign the vertical align for the {@link Component}.
     * @param hAlign the horizontal align for the {@link Component}.
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
     */
    public void setAlign(Align vAlign, Align hAlign)
    {
        align[0] = vAlign;
        align[1] = hAlign;
    }

    /**
     * @return the {@link Orientation} {@link Align} of the {@link Component}.
     */
    public Align[] getAlign()
    {
        return align;
    }

    /**
     * Sets the {@link Align} based on the {@link Orientation}.
     *
     * @param orientation the {@link Orientation} used to determine what {@link Align}
     *                    to set.
     * @param align the {@link Align} for the given {@link Orientation}.
     */
    public void setAlign(Orientation orientation, Align align)
    {
        this.align[orientation.ordinal()] = align;
    }

    /**
     * @param orientation the orientation used to determine what align to return.
     *
     * @return the align based on the orientation.
     */
    public Align getAlign(Orientation orientation)
    {
        return align[orientation.ordinal()];
    }

    /**
     * Sets the {@link Container} containing the {@link Component}.
     *
     * @param container the container containing the component.
     */
    public void setContainer(Container container)
    {
        this.container = container;
    }

    /**
     * @return the {@link Container} containing the {@link Component}.
     */
    public Container getContainer()
    {
        return container;
    }

    /**
     * @return the full width of the {@link Component}.
     */
    public float getFullWidth()
    {
        return getPadding(Direction.LEFT) + getWidth() + getPadding(Direction.RIGHT);
    }

    /**
     * @return the full height of the {@link Component}.
     */
    public float getFullHeight()
    {
        return getPadding(Direction.TOP) + getHeight() + getPadding(Direction.BOTTOM);
    }

    /**
     * @return the width needed by the {@link Component}.
     */
    public float getFullWidthNeeded()
    {
        return getMargin(Direction.LEFT) + getFullWidth() + getMargin(Direction.RIGHT);
    }

    /**
     * @return the height needed by the {@link Component}.
     */
    public float getFullHeightNeeded()
    {
        return getMargin(Direction.TOP) + getFullHeight() + getMargin(Direction.BOTTOM);
    }

    /**
     * @return the full preferred width of the {@link Component}.
     */
    public float getFullPrefWidth()
    {
        return getPadding(Direction.LEFT) + getPrefWidth() + getPadding(Direction.RIGHT);
    }

    /**
     * @return the full preferred height of the {@link Component}.
     */
    public float getFullPrefHeight()
    {
        return getPadding(Direction.TOP) + getPrefHeight() + getPadding(Direction.BOTTOM);
    }

    /**
     * @return the preferred width needed by the {@link Component}.
     */
    public float getFullPrefWidthNeeded()
    {
        return getMargin(Direction.LEFT) + getFullPrefWidth() + getMargin(Direction.RIGHT);
    }

    /**
     * @return the preferred height needed by the {@link Component}.
     */
    public float getFullPrefHeightNeeded()
    {
        return getMargin(Direction.TOP) + getFullPrefHeight() + getMargin(Direction.BOTTOM);
    }
}
