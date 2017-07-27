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

package com.nur1popcorn.irrlicht.engine.events;

import com.sun.xml.internal.ws.api.Cancelable;

import java.lang.reflect.Method;

/**
 * The {@link Priority} this class is used to store data about the method including a handle
 * form which the method will be invoked and the {@link Priority} of the method.
 *
 * @see EventManager
 * @see Priority
 *
 * @author nur1popcorn
 * @since 1.0.1-alpha
 */
public class MethodInfo
{
    private final Method method;
    private final Object handle;
    private final Priority priority;
    private final boolean ignoreCancelled;

    public MethodInfo(Method method, Object handle, Priority priority, boolean ignoreCancelled)
    {
        this.method = method;
        this.handle = handle;
        this.priority = priority;
        this.ignoreCancelled = Cancelable.class.isAssignableFrom(method.getParameterTypes()[0]) && ignoreCancelled;
    }

    /**
     * @return the method which should be invoked.
     */
    public Method getMethod()
    {
        return method;
    }

    /**
     * @return the handle for which the method will be invoked.
     */
    public Object getHandle()
    {
        return handle;
    }

    /**
     * @return the {@link Priority} the method has.
     */
    public Priority getPriority()
    {
        return priority;
    }

    /**
     * @return weather or not the {@link Event} should be ignored if it was cancelled.
     */
    public boolean ignoreCancelled()
    {
        return ignoreCancelled;
    }

    @Override
    public int hashCode()
    {
        return priority.ordinal();
    }
}
