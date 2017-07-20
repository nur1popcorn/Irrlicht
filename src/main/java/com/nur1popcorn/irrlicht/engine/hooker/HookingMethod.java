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

package com.nur1popcorn.irrlicht.engine.hooker;

import com.nur1popcorn.irrlicht.engine.events.Event;
import com.nur1popcorn.irrlicht.engine.events.impl.LocalVariableEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link HookingMethod} is responsible for telling the hooker which and how the
 * method is supposed to be hooked.
 *
 * @see Hooker
 * @see Event
 * @see com.nur1popcorn.irrlicht.engine.wrappers.Wrapper
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HookingMethod
{
    /**
     * @see Event
     *
     * @return the {@link Event} which is supposed to be hooked.
     */
    public Class<? extends Event> value();


    /**
     * @see Hooker
     *
     * @return checks used to find the location at which the hook is supposed to be
     *         added.
     */
    public int flags() default Hooker.DEFAULT | Hooker.BEFORE;

    /**
     * @see Hooker
     *
     * When using the the opcodes flag a number of for the function unique opcodes
     * are required inorder to specify the location which the hook is supposed to
     * be added.
     *
     * @return a number of for the function unique opcodes.
     */
    public int[] opcodes() default { };

    /**
     * @see Hooker
     *
     * @return The indices of the local variables which should be passed to the
     *         {@link Event}'s constructor.
     */
    public int[] indices() default { };

    /**
     * @see Hooker
     *
     * @return This is useful for events which attempt to overwrite local variables ie.
     *         {@link LocalVariableEvent}.
     */
    public int[] overwrite() default { };
}
