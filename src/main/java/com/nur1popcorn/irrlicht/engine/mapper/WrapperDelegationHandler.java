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

import java.lang.reflect.*;
import java.util.stream.Stream;

/**
 * The {@link WrapperDelegationHandler} is responsible for redirecting the {@link Wrapper}'s method
 * calls to the {@link Mapper}'s mapped methods.
 *
 * @see Mapper
 * @see Wrapper
 * @see Proxy
 * @see InvocationHandler
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class WrapperDelegationHandler implements InvocationHandler
{
    private final Object handle;

    //prevent construction :/
    private WrapperDelegationHandler(Object handle)
    {
        this.handle = handle;
    }

    /**
     * Creates a new instance of a {@link Wrapper} using the handle provided.
     *
     * @param wrapper the {@link Wrapper} of which an instance is supposed to be created.
     * @param handle the to which the method calls etc. will be redirected.
     *
     * @return an instance of the {@link Wrapper}.
     */
    public static <T extends Wrapper> T createWrapperProxy(Class<T> wrapper, Object handle)
    {
        return wrapper.cast(Proxy.newProxyInstance(WrapperDelegationHandler.class.getClassLoader(), new Class[] { wrapper }, new WrapperDelegationHandler(handle)));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        if(Wrapper.class.getDeclaredMethods()[0].equals(method))
            return handle;

        final Mapper mapper = Mapper.getInstance();
        final Method obfMethod = mapper.getMappedMethod(method);

        final Object argsHandles[] = args != null ?
                Stream.of(args)
                      .map(arg -> arg instanceof Wrapper ? ((Wrapper) arg).getHandle() : arg)
                      .toArray() :
                null;

        if(obfMethod != null)
        {
            obfMethod.setAccessible(true);
            return convertToType(obfMethod.invoke(handle, argsHandles), method.getReturnType());
        }

        final Field field = mapper.getMappedField(method);
        if(field != null)
        {
            field.setAccessible(true);
            if(method.getReturnType() == void.class)
            {
                field.set(handle, argsHandles[0]);
                return null;
            }
            return convertToType(field.get(handle), method.getReturnType());
        }

        final Constructor constructor = mapper.getMappedConstructor(method);
        if(constructor != null)
        {
            constructor.setAccessible(true);
            return convertToType(constructor.newInstance(argsHandles), method.getReturnType());
        }

        return method.invoke(handle, args);
    }

    /**
     * A helper method for checking if the given object is a {@link Wrapper} and returning a
     * wrapped version of it if so.
     *
     * @param object the object supposed to be wrapped.
     * @param type the type the object is supposed to be converted to.
     *
     * @see #invoke(Object, Method, Object[])
     *
     * @return a wrapped version of the object provided.
     */
    private Object convertToType(Object object, Class type)
    {
        return Wrapper.class.isAssignableFrom(type) ?
               createWrapperProxy((Class<? extends Wrapper>)type, object) :
               object;
    }
}
