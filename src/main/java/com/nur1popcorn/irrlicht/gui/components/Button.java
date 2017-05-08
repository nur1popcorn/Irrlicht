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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@link Button} is a clickable {@link Component}.
 *
 * @see LockableComponent
 * @see Focusable
 * @see Observable
 * @see Observer
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Button extends LockableComponent implements Focusable
{
    private List<ButtonListener> listeners = new ArrayList<>();

    private String text;

    public Button(String text, int width, int height, ButtonListener... listeners)
    {
        super(width, height);
        this.text = text;
        addListener(listeners);
    }

    public Button(String text, ButtonListener... listeners)
    {
        this(text, 35, 9, listeners);
    }

    public Button(ButtonListener... listeners)
    {
        this("", listeners);
    }

    /**
     * @return the {@link Button}'s text.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Sets the {@link Button}'s text.
     *
     * @param text the {@link Button}'s text.
     */
    public void setText(String text)
    {
        this.text = text;
    }

    /**
     * Adds {@link ButtonListener} to the {@link Button}.
     *
     * @param listeners the {@link ButtonListener}s to be added to the {@link Button}.
     */
    public void addListener(ButtonListener... listeners)
    {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    /**
     * Removes {@link ButtonListener}s from {@link Button}.
     *
     * @param listeners the {@link ButtonListener} to be removed from the {@link Button}.
     */
    public void removeListener(ButtonListener... listeners)
    {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    /**
     * Removes all {@link ButtonListener}s from {@link Button}.
     */
    public void clear()
    {
        listeners.clear();
    }

    /**
     * Calls all {@link ButtonListener}.
     */
    public void click()
    {
        listeners.forEach(listener -> listener.call(this));
    }

    /**
     * Can be attached to {@link Button}s inorder to overhear the {@link Button}'s clicks.
     */
    public interface ButtonListener<T extends Button>
    {
        /**
         * Called when the {@link Button} is clicked.
         */
        public void call(T button);
    }
}
