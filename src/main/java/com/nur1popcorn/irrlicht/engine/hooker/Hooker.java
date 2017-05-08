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

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.events.Event;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.entity.PlayerSp;
import com.nur1popcorn.irrlicht.engine.wrappers.client.gui.GuiIngame;
import com.nur1popcorn.irrlicht.utils.ASMUtils;
import com.nur1popcorn.irrlicht.utils.LoggerFactory;
import com.nur1popcorn.irrlicht.utils.TimeHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@link Hooker} is responsible for hooking all of the registered {@link Event}s.
 * In order to use it, all one has to do is create an event class and attach it to a
 * {@link Wrapper}. Or optionally one could resort to adding your own custom check.
 *
 * @see HookingHandler
 * @see HookingMethod
 * @see Event
 * @see Mapper
 * @see Wrapper
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Hooker
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Hooker.class);
    private static Hooker instance;

    /**
     * <b>Custom</b>
     * <p>Will enable any custom check attached to the method.</p>
     *
     * @see #register(Method, HookingHandler...)
     * @see #hook(Instrumentation)
     */
    public static final int CUSTOM = 0x1;

    /**
     * <b>Default</b>
     * <p>This flag will enable any kind of default check.</p>
     * <p><i>Note:</i> All of the checks built into the {@link Hooker} will require
     *    this flag to be enabled for them to work.</p>
     *
     * @see #hook(Instrumentation)
     */
    public static final int DEFAULT = 0x2;

    /**
     * <b>Opcodes</b>
     * <p>Using flag the {@link Hooker} will attempt to find the location at which the
     *    hook is supposed to be placed by looking at a unique set of opcodes.</p>
     *
     * @see HookingMethod#opcodes()
     * @see #hook(Instrumentation)
     */
    public static final int OPCODES = 0x4;

    /**
     * <b>Before</b>
     * <p>This flag will add the hook at the start of the method.</p>
     * <p><i>Note:</i> If used with the {@link #OPCODES} flag it will add the hook
     *    before the specific set of opcodes.</p>
     *
     * @see #hook(Instrumentation)
     */
    public static final int BEFORE = 0x8;

    /**
     * <b>After</b>
     * <p>This flag will add the hook at the end of the method.</p>
     * <p><i>Note:</i> If used with the {@link #OPCODES} flag it will add the hook
     *    after the specific set of opcodes.</p>
     *
     * @see #hook(Instrumentation)
     */
    public static final int AFTER = 0x10;

    private List<Class<? extends Wrapper>> hookingTargets = new ArrayList<>();
    private Map<Method, List<HookingHandler>> hookingHandlers = new HashMap<>();
    private boolean hooked;

    //prevent construction :/
    private Hooker()
    {}

    /**
     * Factory method to create a default {@link Hooker}.
     *
     * @see #getInstance()
     *
     * @return a default {@link Hooker}.
     */
    private static Hooker createHooker()
    {
        final Hooker hooker = new Hooker();
        hooker.register(PlayerSp.class);
        hooker.register(GuiIngame.class);
        return hooker;
    }

    /**
     * @return an instance of the {@link Hooker} or if non is available it creates
     *         one.
     */
    public static Hooker getInstance()
    {
        return instance != null ? instance : (instance = createHooker());
    }

    /**
     * Registers a {@link Wrapper} which is supposed to be hooked.
     *
     * @param wrapperClass the {@link Wrapper} which is supposed to be hooked.
     *
     * @see #hook(Instrumentation)
     */
    public void register(Class<? extends Wrapper> wrapperClass)
    {
        hookingTargets.add(wrapperClass);
    }

    /**
     * Unregisters a {@link Wrapper} which is supposed to be hooked.
     *
     * @param wrapperClass the {@link Wrapper} which is supposed to be hooked.
     *
     * @see #hook(Instrumentation)
     */
    public void unregister(Class<? extends Wrapper> wrapperClass)
    {
        hookingTargets.remove(wrapperClass);
    }

    /**
     * Adds a custom {@link HookingHandler} to the method selected.
     *
     * @param method the method to which the {@link HookingHandler} is supposed to be
     *               added.
     * @param hookingHandlers the {@link HookingHandler} which are supposed to get
     *                        added to the method.
     *
     * @see #hook(Instrumentation)
     */
    public void register(Method method, HookingHandler... hookingHandlers)
    {
        this.hookingHandlers.put(method, Arrays.asList(hookingHandlers));
    }

    /**
     * Removes a custom {@link HookingHandler} from the method selected.
     *
     * @param method the method from which the custom {@link HookingHandler} is
     *               supposed to be removed.
     *
     * @see #hook(Instrumentation)
     */
    public void unregister(Method method)
    {
        hookingHandlers.remove(method);
    }

    /**
     * <p>This method will place all the required hooks.</p>
     * <p><i>Note:</i> This method should only be called once.</p>
     *
     * @param instrumentation used to instrument java-bytecode.
     */
    public void hook(Instrumentation instrumentation)
    {
        if(hooked)
            return;
        final TimeHelper timeHelper = new TimeHelper();
        LOGGER.log(Level.INFO, "Started hooking classes.");
        try
        {
            final Mapper mapper = Mapper.getInstance();
            instrumentation.redefineClasses(hookingTargets.stream().map(clazz -> {
                LOGGER.log(Level.INFO, "Hooking class " + clazz + ".");
                for(Method method : clazz.getDeclaredMethods())
                {
                    final HookingMethod hookingMethod = method.getDeclaredAnnotation(HookingMethod.class);
                    if(hookingMethod != null)
                    {
                        final int flags = hookingMethod.flags();
                        final MethodNode methodNode = ASMUtils.getMethodNode(mapper.getMappedMethod(method));
                        assert methodNode != null;
                        if((flags & DEFAULT) != 0)
                        {
                            final InsnList injection = new InsnList();
                            {
                                //setup injection
                                final String value = Type.getInternalName(hookingMethod.value());
                                injection.add(new TypeInsnNode(Opcodes.NEW, value));
                                injection.add(new InsnNode(Opcodes.DUP));
                                injection.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, value, "<init>", "()V", false));
                                final String event = Type.getInternalName(Event.class);
                                injection.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EventManager.class), "call", "(L" + event + ";)L" + event + ";", false));
                            }
                            //check injection method.
                            if((flags & OPCODES) != 0 &&
                               hookingMethod.opcodes().length != 0)
                                //find set of opcodes and inject before or after them.
                                ASMUtils.insert(methodNode.instructions, hookingMethod.opcodes(), injection, (flags & BEFORE) != 0);
                            else if((flags & BEFORE) != 0)
                                //insert injection at method start.
                                methodNode.instructions.insert(injection);
                            else if((flags & AFTER) != 0)
                                //insert injection at method end.
                                methodNode.instructions.insertBefore(ASMUtils.getLast(methodNode.instructions, Opcodes.RETURN), injection);
                        }

                        if((flags & CUSTOM) != 0)
                            for(HookingHandler hookingHandler : hookingHandlers.get(method))
                                hookingHandler.hook(methodNode.instructions);
                        LOGGER.log(Level.INFO, "    Hooking method " + method + ".");
                    }
                }
                final Class mappedClass = mapper.getMappedClass(clazz);
                return new ClassDefinition(mappedClass, ASMUtils.getBytes(ASMUtils.getClassNode(mappedClass)));
            }).toArray(ClassDefinition[]::new));
        }
        catch (ClassNotFoundException | UnmodifiableClassException e)
        {
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO, "Finished hooking classes in: " + timeHelper.getMSPassed() + "ms.");
        hooked = true;
    }

    /**
     * @return whether or not the hooking process was successful.
     */
    public boolean isHooked()
    {
        return hooked;
    }
}