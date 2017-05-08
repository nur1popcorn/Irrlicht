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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@link Container} is used to store and layout other {@link Component}s.
 *
 * @see Component
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Container extends Component
{
    protected List<Component> components = new ArrayList<>();

    public Container()
    {}

    public Container(Component... components)
    {
        add(components);
    }

    /**
     * Adds {@link Component}s to the {@link Container}.
     *
     * @param components the {@link Component}s which are to be added to the list.
     */
    public void add(Component... components)
    {
        this.components.addAll(Arrays.asList(components));
        for(Component component : components)
            component.setContainer(this);
        layoutComponents();
    }

    /**
     * Removes given {@link Component}s from the {@link Container}.
     *
     * @param components the {@link Component}s which are to be removed from the list.
     */
    public void remove(Component... components)
    {
        this.components.removeAll(Arrays.asList(components));
        layoutComponents();
    }

    /**
     * Removes all {@link Component}s from the {@link Container}.
     */
    public void clear()
    {
        components.clear();
        layoutComponents();
    }

    /**
     * @return all {@link Component}s from the {@link Container}.
     */
    public List<Component> getComponents()
    {
        return components;
    }

    /**
     * Used to layout all {@link Component}s.
     */
    public void layoutComponents()
    {
        for(Component component : components)
        {
            component.setWidth(component.isFittingWidth() ? width : component.getPrefWidth());
            component.setHeight(component.isFittingHeight() ? height : component.getPrefHeight());

            component.setX(x + component.getPrefX());
            component.setY(y + component.getPrefY());

            if(component instanceof Container)
                ((Container)component).layoutComponents();
        }
    }
}
