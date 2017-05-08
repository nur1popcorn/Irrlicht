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
import com.nur1popcorn.irrlicht.gui.components.Focusable;
import com.nur1popcorn.irrlicht.gui.components.containers.Container;

/**
 * The {@link Frame} is a movable {@link Container}.
 *
 * @see Container
 * @see Component
 * @see Focusable
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Frame extends Container implements Focusable
{
    private String title;
    private float dragX,
                  dragY;

    private boolean draggable,
                    dragging;

    public Frame(String title)
    {
        this.title = title;
    }

    public Frame(String title, int x, int y, int width, int height, Component... components)
    {
        super(components);
        this.title = title;
        prefX = x;
        prefY = y;
        prefWidth = width;
        prefHeight = height;
    }
    /**
     * Sets the {@link Frame}'s title.
     *
     * @param title the {@link Frame}'s title.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return the {@link Frame}'s title.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Sets x offset for the {@link Frame} when being dragged.
     *
     * @param dragX the x offset for the {@link Frame} when being dragged.
     */
    public void setDragX(float dragX)
    {
        this.dragX = dragX;
    }

    /**
     * Sets y offset for {@link Frame} when being dragged.
     *
     * @param dragY the y offset for the {@link Frame} when being dragged.
     */
    public void setDragY(float dragY)
    {
        this.dragY = dragY;
    }

    /**
     * @return the x offset for the {@link Frame} when being dragged.
     */
    public float getDragX()
    {
        return dragX;
    }

    /**
     * @return the y offset for the {@link Frame} when being dragged.
     */
    public float getDragY()
    {
        return dragY;
    }

    /**
     * Sets whether or not the {@link Frame} is draggable.
     *
     * @param draggable whether or not the {@link Frame} is draggable.
     */
    public void setDraggable(boolean draggable)
    {
        this.draggable = draggable;
    }

    /**
     * @return whether or not the {@link Frame} is draggable.
     */
    public boolean isDraggable()
    {
        return draggable;
    }

    /**
     * Sets whether or not the {@link Frame} is currently being dragged.
     *
     * @param dragging whether or not the {@link Frame} is currently being dragged.
     */
    public void setDragging(boolean dragging)
    {
        this.dragging = dragging;
    }

    /**
     * @return whether or not the {@link Frame} is currently being dragged.
     */
    public boolean isDragging()
    {
        return dragging;
    }
}
