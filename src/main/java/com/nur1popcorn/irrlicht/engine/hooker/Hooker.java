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
import com.nur1popcorn.irrlicht.engine.events.EventManager;
import com.nur1popcorn.irrlicht.engine.events.ICancellableEvent;
import com.nur1popcorn.irrlicht.engine.events.ILocalVariableEvent;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.entity.PlayerSp;
import com.nur1popcorn.irrlicht.engine.wrappers.client.gui.GuiIngame;
import com.nur1popcorn.irrlicht.engine.wrappers.client.minecraft.Timer;
import com.nur1popcorn.irrlicht.engine.wrappers.client.network.NetworkManager;
import com.nur1popcorn.irrlicht.utils.ASMUtils;
import com.nur1popcorn.irrlicht.utils.LoggerFactory;
import com.nur1popcorn.irrlicht.management.TimeHelper;
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
 * @see EventManager
 * @see Mapper
 * @see Wrapper
 * @see ASMUtils
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Hooker
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Hooker.class);

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

    //prevent construction :/
    private Hooker()
    {}

    /**
     * Factory method to create a default {@link Hooker}.
     *
     * @return a default {@link Hooker}.
     */
    public static Hooker createHooker()
    {
        final Hooker hooker = new Hooker();
        hooker.register(Mapper.DisplayWrapper.class); //hook #swapBuffers method.
        hooker.register(PlayerSp.class);
        hooker.register(GuiIngame.class);
        hooker.register(NetworkManager.class);
        hooker.register(Timer.class);
        return hooker;
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
                        final MethodNode methodNode = mapper.getMappedMethod(method) != null ?
                                ASMUtils.getMethodNode(mapper.getMappedMethod(method)) :
                                ASMUtils.getMethodNode(mapper.getMappedConstructor(method));
                        assert methodNode != null;
                        if((flags & DEFAULT) != 0)
                        {
                            final InsnList injection = new InsnList();
                            {
                                //setup injection.
                                final LabelNode start = new LabelNode();
                                final LabelNode end = new LabelNode();
                                injection.add(start);
                                final Class eventClass = hookingMethod.value();
                                final String value = Type.getInternalName(eventClass);
                                {
                                    //call event constructor.
                                    injection.add(new TypeInsnNode(Opcodes.NEW, value));
                                    injection.add(new InsnNode(Opcodes.DUP));
                                    injection.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, value, "<init>", "()V", false));
                                }

                                final String object = Type.getInternalName(Object.class);
                                final boolean localVar = ILocalVariableEvent.class.isAssignableFrom(eventClass);
                                if(localVar)
                                {
                                    //hijack local variables.
                                    final int index = methodNode.localVariables.size();
                                    methodNode.localVariables.add(new LocalVariableNode(eventClass.getSimpleName().toLowerCase(), "L" + value + ";", null, start, end, index));
                                    injection.add(new VarInsnNode(Opcodes.ASTORE, index));
                                    injection.add(new VarInsnNode(Opcodes.ALOAD, index));

                                    final int indices[] = hookingMethod.indices();
                                    injection.add(new IntInsnNode(Opcodes.BIPUSH, indices.length));
                                    injection.add(new TypeInsnNode(Opcodes.ANEWARRAY, object));
                                    injection.add(new InsnNode(Opcodes.DUP));

                                    for(int i = 0; i < indices.length; i++)
                                    {
                                        injection.add(new IntInsnNode(Opcodes.BIPUSH, i));
                                        injection.add(new VarInsnNode(Opcodes.ALOAD, indices[i]));
                                        injection.add(new InsnNode(Opcodes.AASTORE));
                                        if(i < indices.length - 1)
                                            injection.add(new InsnNode(Opcodes.DUP));
                                    }
                                    injection.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, value, "setLocalVariables", "([L" + object + ";)V", false));

                                    injection.add(new VarInsnNode(Opcodes.ALOAD, index));
                                }

                                final String event = Type.getInternalName(Event.class);
                                injection.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EventManager.class), "call", "(L" + event + ";)L" + event + ";", false));

                                if(method.getReturnType() == void.class &&
                                   ICancellableEvent.class.isAssignableFrom(eventClass))
                                {
                                    //check if was cancelled and if so leave method.
                                    injection.add(new TypeInsnNode(Opcodes.CHECKCAST, value));
                                    injection.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, value, "isCancelled", "()Z", false));
                                    final LabelNode labelNode = new LabelNode();
                                    injection.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
                                    injection.add(new InsnNode(Opcodes.RETURN));
                                    injection.add(labelNode);
                                }

                                if(localVar)
                                {
                                    //replace old local variables.
                                    final int index = methodNode.localVariables.size();
                                    final LabelNode labelNode = new LabelNode();
                                    injection.add(labelNode);
                                    methodNode.localVariables.add(new LocalVariableNode("localVariables", "[L" + object + ";", null, labelNode, end, index));
                                    injection.add(new VarInsnNode(Opcodes.ALOAD, index - 1));
                                    injection.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, value, "getLocalVariables", "()[L" + object + ";", false));
                                    injection.add(new VarInsnNode(Opcodes.ASTORE, index));

                                    final int overwrite[] = hookingMethod.overwrite();
                                    for(int i = 0; i < overwrite.length; i++)
                                    {
                                        injection.add(new VarInsnNode(Opcodes.ALOAD, index));
                                        injection.add(new IntInsnNode(Opcodes.BIPUSH, i));
                                        injection.add(new InsnNode(Opcodes.AALOAD));
                                        final String desc = ((LocalVariableNode) methodNode.localVariables.get(overwrite[i])).desc;
                                        final Type type = Type.getType(desc);
                                        final int opcode = type.getOpcode(Opcodes.ISTORE);
                                        if(opcode == Opcodes.ASTORE)
                                            injection.add(new TypeInsnNode(Opcodes.CHECKCAST, type.getInternalName()));
                                        injection.add(new VarInsnNode(opcode, overwrite[i]));
                                    }
                                }

                                injection.add(end);
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
                                hookingHandler.hook(methodNode);
                        LOGGER.log(Level.INFO, "    Hooking method " + method + ".");
                    }
                }
                final Class mappedClass = mapper.getMappedClass(clazz);
                return new ClassDefinition(mappedClass, ASMUtils.getBytes(ASMUtils.getClassNode(mappedClass)));
            }).toArray(ClassDefinition[]::new));
        }
        catch(ClassNotFoundException | UnmodifiableClassException e)
        {
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO, "Finished hooking classes in: " + timeHelper.getMSPassed() + "ms.");
    }
}