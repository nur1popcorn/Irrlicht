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

package com.nur1popcorn.irrlicht.engine.mapper;

import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.gui.GuiScreen;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The {@link WrapperBridge} is responsible for redirecting method calls to an obfuscated
 * class to the implementation of the {@link Wrapper} it's mapped to.
 *
 * @see ProxyFactory
 * @see Mapper
 * @see Wrapper
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
public abstract class WrapperBridge implements Wrapper
{
    private Object handle;
    private final Class<? extends Wrapper> clazz;

    public WrapperBridge(Class<? extends Wrapper> clazz)
    {
        this.clazz = clazz;
    }

    @Override
    public final Object getHandle()
    {
        if(handle != null)
            return handle;
        final ProxyFactory proxyFactory = new ProxyFactory();
        final Mapper mapper = Mapper.getInstance();
        proxyFactory.setSuperclass(mapper.getMappedClass(clazz));
        try
        {
            return handle = proxyFactory.create(null, null, (Object proxy, Method thisMethod, Method proceed, Object[] args) -> {
                for(Method method : GuiScreen.class.getDeclaredMethods())
                    if(mapper.getMappedMethod(method).equals(thisMethod))
                    {
                        final Class types[] = method.getParameterTypes();
                        final Object handledArgs[] = new Object[args.length];
                        for(int i = 0; i < args.length; i++)
                            handledArgs[i] = Wrapper.class.isAssignableFrom(types[i]) ?
                                    WrapperDelegationHandler.createWrapperProxy((Class<? extends Wrapper>)types[i], args[i]) :
                                    args[i];
                        return method.invoke(this, handledArgs);
                    }
                return null;
            });
        }
        catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to create WrapperBridge for: " );
        }
    }
}
