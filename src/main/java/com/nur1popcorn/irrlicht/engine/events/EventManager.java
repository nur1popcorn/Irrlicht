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

import com.nur1popcorn.irrlicht.engine.events.impl.CancellableEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * The {@link EventManager} is used to dispatch {@link Event}s.
 *
 * @see MethodInfo
 * @see Priority
 * @see Event
 * @see EventTarget
 *
 * @author nur1popcorn
 * @since 1.0.1-alpha
 */
public class EventManager
{
    private static final Map<Class<? extends Event>, Set<MethodInfo>> EVENT_REGISTRY = new HashMap<>();

    //prevent construction :/
    private EventManager()
    {}

    /**
     * Registers all of the event-handlers of a instance.
     *
     * @param handle the object that contains the methods one wants to register and the
     *               handle used to invoke these methods.
     * @param events the specific {@link Event}s which are supposed to be registered, if
     *               empty, it will register any type of {@link Event}.
     *
     * @see #call(Event)
     */
    public static void register(Object handle, Class<? extends Event>... events)
    {
        for(Method method : handle.getClass().getDeclaredMethods())
            if(isValid(method, events))
            {
                final Class<? extends Event> eventType = (Class<? extends Event>) method.getParameterTypes()[0];
                EVENT_REGISTRY.putIfAbsent(eventType, new HashSet<>());
                final EventTarget eventTarget = method.getDeclaredAnnotation(EventTarget.class);
                EVENT_REGISTRY.get(eventType)
                              .add(new MethodInfo(method, handle, eventTarget.priority(), eventTarget.ignoreCancelled()));
            }
    }

    /**
     * Unregisters all of the event-handlers of a instance.
     *
     * @param handle the handle for which one wants to unregister all the methods.
     * @param events the specific {@link Event}s which are supposed to be unregistered, if
     *               empty, it will unregister any type of {@link Event}.
     *
     * @see #call(Event)
     */
    public static void unregister(Object handle, Class<? extends Event>... events)
    {
        for(Method method : handle.getClass().getDeclaredMethods())
        {
            final Class<? extends Event> eventType;
            final Set<MethodInfo> methodInfos;
            if(isValid(method, events) &&
              (methodInfos = EVENT_REGISTRY.get(eventType = (Class<? extends Event>) method.getParameterTypes()[0])) != null)
            {
                methodInfos.remove(method);
                if(methodInfos.isEmpty())
                    EVENT_REGISTRY.remove(eventType);
            }
        }
    }

    /**
     * @param method the method which should be checked.
     * @param events the {@link Event} types which are valid, if empty, any type of
     *               {@link Event} is valid.
     *
     * @return whether or not a method is a {@link Event}-handler.
     */
    private static boolean isValid(Method method, Class<? extends Event>... events)
    {
        return method.isAnnotationPresent(EventTarget.class) &&
               method.getParameterTypes().length == 1 &&
               Event.class.isAssignableFrom(method.getParameterTypes()[0]) &&
               (events.length == 0 ||
                Stream.of(events)
                      .anyMatch(event -> event == method.getParameterTypes()[0]));
    }

    /**
     * Removes all event-handlers.
     */
    public static void clear()
    {
        EVENT_REGISTRY.clear();
    }

    /**
     * Calls all event-handlers listening for this {@link Event}.
     *
     * @param event the {@link Event} which should be called.
     *
     * @return the {@link Event} that was passed to the function with the modifications
     *         of the event-handlers applied.
     */
    public static <T extends Event> T call(T event)
    {
        final Set<MethodInfo> methodInfos = EVENT_REGISTRY.get(event.getClass());
        if(methodInfos != null)
           methodInfos.forEach(methodInfo -> {
                try
                {
                    if(!methodInfo.ignoreCancelled() ||
                       !((CancellableEvent) event).isCancelled())
                    {
                        final Method method = methodInfo.getMethod();
                        method.setAccessible(true);
                        method.invoke(methodInfo.getHandle(), event);
                    }
                }
                catch(IllegalAccessException | InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            });
        return event;
    }
}
