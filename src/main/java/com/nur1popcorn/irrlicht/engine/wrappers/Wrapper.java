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

package com.nur1popcorn.irrlicht.engine.wrappers;

import java.lang.reflect.Method;

/**
 * The {@link Wrapper}s are a way of making the obfuscated classes easily accessible.
 *
 * @see com.nur1popcorn.irrlicht.engine.mapper.Mapper
 * @see com.nur1popcorn.irrlicht.engine.mapper.WrapperDelegationHandler
 * @see com.nur1popcorn.irrlicht.engine.hooker.Hooker
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public interface Wrapper
{
    /**
     * The instance to which the method calls will be redirected.
     *
     * @see com.nur1popcorn.irrlicht.engine.mapper.WrapperDelegationHandler#createWrapperProxy(Class, Object)
     * @see com.nur1popcorn.irrlicht.engine.mapper.WrapperDelegationHandler#invoke(Object, Method, Object[])
     *
     * @return the {@link Wrapper}'s handle.
     */
    public Object getHandle();
}
