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
    private Method method;
    private Object handle;
    private Priority priority;

    public MethodInfo(Method method, Object handle, Priority priority)
    {
        assert method != null && handle != null && priority != null;
        this.method = method;
        this.handle = handle;
        this.priority = priority;
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

    @Override
    public int hashCode()
    {
        //used to put the methodinfos in the correct order.
        return priority.ordinal();
    }
}
