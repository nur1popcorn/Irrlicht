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
import com.nur1popcorn.irrlicht.gui.components.layout.Orientation;

/**
 * The {@link ScrollContainer} is used to expand a {@link Container} beyond their natural
 * bounds.
 *
 * @see Container
 * @see Component
 * @see Orientation
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class ScrollContainer extends Container
{
    public static int sliderSize = 0;
    private float scroll[] = new float[2],
                  realWidth,
                  realHeight,
                  dragX,
                  dragY;

    private boolean dragging[] = new boolean[2];

    public ScrollContainer(Component... components)
    {
        add(components);
        setFitWidth(true);
        setFitHeight(true);
    }

    /**
     * Sets the vertical and horizontal scroll for the scroll pane.
     *
     * @param orientation the orientation for which the scroll should be set.
     * @param scroll the scroll which should be used.
     */
    public void setScroll(Orientation orientation, float scroll)
    {
        this.scroll[orientation.ordinal()] = scroll;
    }

    /**
     * @return the vertical and horizontal scroll for the scroll pane.
     */
    public float[] getScroll()
    {
        return scroll;
    }

    /**
     * @return the scroll for the orientation provided.
     */
    public float getScroll(Orientation orientation)
    {
        return scroll[orientation.ordinal()];
    }

    /**
     * Sets the containers real width.
     *
     * @param realWidth the containers real width.
     */
    public void setRealWidth(float realWidth)
    {
        this.realWidth = realWidth;
    }

    /**
     * @return the containers real width.
     */
    public float getRealWidth()
    {
        return realWidth;
    }

    /**
     * Sets the containers real height.
     *
     * @param realHeight the containers real height.
     */
    public void setRealHeight(float realHeight)
    {
        this.realHeight = realHeight;
    }

    /**
     * @return the containers real height.
     */
    public float getRealHeight()
    {
        return realHeight;
    }

    /**
     * Starts dragging and specifies in which orientation the container is being dragged.
     *
     * @param orientation the orientation in which the container should be dragged.
     */
    public void setDragging(Orientation orientation)
    {
        this.dragging = new boolean[2];
        this.dragging[orientation.ordinal()] = true;
    }

    /**
     * Stops dragging.
     */
    public void stopDragging()
    {
        dragging = new boolean[2];
    }

    /**
     * @return whether or not the container is being dragged.
     */
    public boolean isDragging()
    {
        return dragging[0] | dragging[1];
    }

    /**
     * @return the orientation in which the container is being dragged.
     */
    public Orientation getDrag()
    {
        return dragging[0] ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    /**
     * Sets the drag offset of the x position.
     *
     * @param dragX the drag offset of the x position.
     */
    public void setDragX(float dragX)
    {
        this.dragX = dragX;
    }

    /**
     * @return the drag offset of the x position.
     */
    public float getDragX()
    {
        return dragX;
    }

    /**
     * Sets the the drag offset of the y position.
     *
     * @param dragY the the drag offset of the y position.
     */
    public void setDragY(float dragY)
    {
        this.dragY = dragY;
    }

    /**
     * @return the the drag offset of the y position.
     */
    public float getDragY()
    {
        return dragY;
    }

    @Override
    public void layoutComponents()
    {
        realWidth = 0;
        realHeight = 0;
        for(Component component : components)
        {
            component.setWidth(component.isFittingWidth() ? width : component.getPrefWidth());
            component.setHeight(component.isFittingHeight() ? height : component.getPrefHeight());

            if(component instanceof Container)
                ((Container)component).layoutComponents();

            realWidth = Math.max(component.getPrefX() + component.getFullWidthNeeded() > realWidth ? component.getPrefX() + component.getFullWidthNeeded() : component.getPrefX() + component.getFullWidthNeeded() + sliderSize, realWidth);
            realHeight = Math.max(component.getPrefY() + component.getFullHeightNeeded() > realHeight ? component.getPrefY() + component.getFullHeightNeeded() : component.getPrefY() + component.getFullHeightNeeded() + sliderSize, realHeight);
        }

        for(Component component : components)
        {
            component.setWidth(component.isFittingWidth() ? width - (getY() - component.getY() + component.getFullHeightNeeded() > getFullHeightNeeded() ? sliderSize : 0) : component.getPrefWidth());
            component.setHeight(component.isFittingHeight() ? height - (getX() - component.getX() + component.getFullWidthNeeded() > getFullWidthNeeded() ? sliderSize : 0) : component.getPrefHeight());

            if(component instanceof Container)
                ((Container)component).layoutComponents();

            component.setX(realWidth > 0 ? x + component.getPrefX() - scroll[1] * realWidth / (getFullWidth() - sliderSize) : x + component.getPrefX());
            component.setY(realHeight > 0 ? y + component.getPrefY() - scroll[0] * realHeight / (getFullHeight() - sliderSize) : y + component.getPrefY());
        }
    }
}