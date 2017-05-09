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

package com.nur1popcorn.irrlicht.modules;

import com.nur1popcorn.irrlicht.engine.events.EventManager;
import com.nur1popcorn.irrlicht.management.values.Value;
import com.nur1popcorn.irrlicht.management.values.ValueTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * The {@link Module} is a cheat that can be enabled.
 *
 * @see Category
 * @see Value
 * @see ValueTarget
 * @see EventManager
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Module
{
    private final String name = getClass().getAnnotation(ModuleInfo.class).name();
    private final Category category = getClass().getAnnotation(ModuleInfo.class).category();
    private final List<Value> values = new ArrayList<>();
    private boolean toggled;

    /**
     * @see Value
     * @see ValueTarget
     *
     * Obtains all of the {@link Module}'s {@link Value}s if they have a
     * {@link ValueTarget} annotaion attached to them.
     */
    public final void init()
    {
        Stream.of(getClass().getDeclaredFields()).forEach(field -> {
            if(field.getAnnotationsByType(ValueTarget.class) != null &&
               Value.class.isAssignableFrom(field.getType()))
                try
                {
                    field.setAccessible(true);
                    values.add((Value) field.get(this));
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
        });
    }

    /**
     * @see EventManager
     *
     * Called when the {@link Module} is toggled.
     */
    public void onEnable()
    {
        EventManager.register(this);
    }

    /**
     * @see EventManager
     *
     * Called when the {@link Module} is toggled off.
     */
    public void onDisable()
    {
        EventManager.unregister(this);
    }

    /**
     * Toggles the {@link Module} on or off.
     */
    public void toggle()
    {
        toggled = !toggled;
        if(toggled)
            onEnable();
        else
            onDisable();
    }

    /**
     * @return whether or not the {@link Module} is toggled.
     */
    public boolean isToggled()
    {
        return toggled;
    }

    /**
     * @return the {@link Module}'s name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @see Category
     *
     * @return the {@link Module}'s {@link Category}.
     */
    public Category getCategory()
    {
        return category;
    }

    /**
     * @see Value
     *
     * @return the {@link Module}'s settings.
     */
    public List<Value> getValues()
    {
        return values;
    }
}
