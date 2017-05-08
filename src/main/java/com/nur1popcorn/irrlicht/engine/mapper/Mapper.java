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

import com.nur1popcorn.irrlicht.engine.exceptions.MappingException;
import com.nur1popcorn.irrlicht.engine.wrappers.Start;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;
import com.nur1popcorn.irrlicht.engine.wrappers.client.entity.ClientPlayer;
import com.nur1popcorn.irrlicht.engine.wrappers.client.entity.PlayerSp;
import com.nur1popcorn.irrlicht.engine.wrappers.client.gui.GuiIngame;
import com.nur1popcorn.irrlicht.engine.wrappers.client.gui.GuiScreen;
import com.nur1popcorn.irrlicht.engine.wrappers.client.minecraft.Timer;
import com.nur1popcorn.irrlicht.engine.wrappers.client.network.NetHandler;
import com.nur1popcorn.irrlicht.engine.wrappers.client.settings.GameSettings;
import com.nur1popcorn.irrlicht.engine.wrappers.entity.Entity;
import com.nur1popcorn.irrlicht.engine.wrappers.entity.EntityLivingBase;
import com.nur1popcorn.irrlicht.engine.wrappers.entity.EntityPlayer;
import com.nur1popcorn.irrlicht.engine.wrappers.entity.PlayerAbilities;
import com.nur1popcorn.irrlicht.engine.wrappers.util.AxisAlignedBB;
import com.nur1popcorn.irrlicht.utils.ASMUtils;
import com.nur1popcorn.irrlicht.utils.LoggerFactory;
import com.nur1popcorn.irrlicht.utils.TimeHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@link Mapper} is responsible for creating and storing the mappings for all of the
 * registered {@link Wrapper}s. It also provides a number of very useful flags.
 *
 * @see DiscoveryHandler
 * @see DiscoveryMethod
 * @see WrapperDelegationHandler
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Mapper
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Mapper.class);
    private static Mapper instance;

    /**
     * <b>Custom</b>
     * <p>This flag will enable any custom check attached to the {@link Wrapper}s.</p>
     *
     * @see #register(Class, DiscoveryHandler[])
     * @see #register(Method, DiscoveryHandler[])
     * @see #generate()
     */
    public static final int CUSTOM = 0x1;

    /**
     * <b>Default</b>
     * <p>This flag will enable any kind of default check built in.</p>
     * <p><i>Note:</i> All of the checks built into the {@link Mapper} will require this flag
     *    to be enabled for them to work.</p>
     * <p><i>Note:</i> If attached to a method it attempts to find it based on
     *    these factors.</p>
     * <ul>
     *     <li>ReturnType</li>
     *     <li>Modifiers</li>
     *     <li>Parameters</li>
     *     <li>Exceptions</li>
     * </ul>
     *
     * @see DiscoveryMethod#modifiers()
     * @see #generate()
     */
    public static final int DEFAULT = 0x2;

    /**
     * <b>Field</b>
     * <p>This flag will define a method as a getter or a setter for a field.
     *    <i>This field will be matched based on these factors:</i></p>
     * <ul>
     *     <li>Type</li>
     *     <li>Modifiers</li>
     *     <li>Structure</li>
     * </ul>
     *
     * @see DiscoveryMethod#modifiers()
     * @see #generate()
     */
    public static final int FIELD = 0x4;

    /**
     * <b>Constructor</b>
     * <p>This flag is used to find a class through the provided declaring classes
     *    fields by looking at the field's type's constructors:</p>
     * <pre>
     * +-------+      provides      +-----------------+
     * |Wrapper+---------|--------->+Ideal Constructor|
     * +--+----+                    +------+----------+
     *    v provides                       v match
     * +--+------------+ +------+ +----+ +-+----------+
     * |Declaring Class+>+Fields+>+Type+>+Constructors|
     * +---------------+ +------+ +----+ +------------+
     * </pre>
     *
     * @see DiscoveryMethod#declaring()
     * @see #generate()
     */
    public static final int CONSTRUCTOR = 0x8;

    /**
     * <b>Structure Start</b>
     * <p>This flag will assume that the method it is attached to is the start of a
     *    structure which is unlikely to change.</p>
     * <p><i>Note:</i> It is very important to end the structure since every method in
     *    between start and end will become a part of the structure.</p>
     * <ul>
     *     <li>
     *         <p>The order of fields.</p>
     *         <p><strong>Example:</strong> the fields:</p>
     *         <ul>
     *             <li>x</li>
     *             <li>y</li>
     *             <li>z</li>
     *         </ul>
     *         <p>are provided this order is very unlikely to change it wouldn't make
     *            sense to reorder them.</p>
     *     </li>
     *     <li>This will assume that the number of constructors provided is the number
     *         of constructors needed for the class to be valid.</li>
     * </ul>
     *
     * @see #generate()
     */
    public static final int STRUCTURE_START = 0x10;

    /**
     * <b>Structure End</b>
     * <p>This flag will mark the end of a started structure.</p>
     *
     * @see #generate()
     */
    public static final int STRUCTURE_END = 0x20;

    /**
     * <b>String Const</b>
     * <p>This flag will search the class or methods provided for a string constant.</p>
     *
     * @see DiscoveryMethod#constants()
     * @see DiscoveryMethod#declaring()
     * @see #generate()
     */
    public static final int STRING_CONST = 0x40;

    /**
     * <b>Extension</b>
     * <p>This flag look at the declaring class's superclass.</p>
     *
     * @see DiscoveryMethod#declaring()
     * @see #generate()
     */
    public static final int EXTENSION = 0x80;

    /**
     * <b>First Match</b>
     * <p>This flag will declare the first match the final one.</p>
     * <p><i>Note:</i> It will also ignore the number of other possible matches by
     *    default the search would be terminated if there is more than 1 match.</p>
     *
     * @see #generate()
     */
    public static final int FIRST_MATCH = 0x100;

    /**
     * <b>Last Match</b>
     * <p>This flag will declare the last match the final one.</p>
     * <p><i>Note:</i> It will also ignore the number of other possible matches by
     *    default the search would be terminated if there is more than 1 match.</p>
     *
     * @see #generate()
     */
    public static final int LAST_MATCH = 0x200;

    /**
     * <b>Opcodes</b>
     * <p>This flag will search for the method using the opcodes provided.</p>
     *
     * @see DiscoveryMethod#opcodes()
     * @see #generate()
     */
    public static final int OPCODES = 0x400;

    private Map<Class<? extends Wrapper>, List<DiscoveryHandler<? extends Class>>> wrapperClasses = new LinkedHashMap<>();
    private Map<Method, List<DiscoveryHandler>> customDiscoveryMethods = new HashMap<>();
    private Map<Class<? extends Wrapper>, Class> mappedClasses = new HashMap<>();
    private Map<Method, Method> mappedMethods = new HashMap<>();
    private Map<Method, Field> mappedFields = new HashMap<>();
    private boolean success;

    //prevent construction :/
    private Mapper()
    {}

    /**
     * A Factory method used to create a default {@link Mapper}.
     *
     * @see #getInstance()
     *
     * @return a default {@link Mapper}.
     */
    private static Mapper createMapper()
    {
        final Mapper mapper = new Mapper();
        mapper.register(Start.class, clazz -> Class.forName(Start.DEFAULT_LOC));
        mapper.register(Minecraft.class, m -> {
            final Class start = m.getMappedClass(Start.class);
            if(start != null)
                return Class.forName(
                        ((TypeInsnNode)ASMUtils.getLastInstruction(
                                ASMUtils.getMethodNode(ASMUtils.getMethod(m.getMappedClass(Start.class), "main([Ljava/lang/String;)V")).instructions,
                                Opcodes.NEW)).desc.replace("/", "."));
            throw new ClassNotFoundException("Could not find minecraft class.");
        });
        mapper.register(ASMUtils.getMethod(Minecraft.class, "getLeftClickDelay()I"), m -> {
            final Method method = m.getMappedMethod(ASMUtils.getMethod(Minecraft.class, "clickMouse()V"));
            if(method != null)
            {
                final Field field = m.getMappedClass(Minecraft.class).getDeclaredField(((FieldInsnNode)ASMUtils.getFirst(ASMUtils.getMethodNode(method).instructions, Opcodes.GETFIELD)).name);
                m.mappedFields.put(ASMUtils.getMethod(Minecraft.class, "setLeftClickDelay(I)V"), field);
                return field;
            }
            return null;
        });
        mapper.register(PlayerSp.class);
        mapper.register(ClientPlayer.class);
        mapper.register(EntityPlayer.class);
        mapper.register(PlayerAbilities.class);
        mapper.register(EntityLivingBase.class);
        mapper.register(Entity.class);
        mapper.register(AxisAlignedBB.class);
        mapper.register(Timer.class);
        mapper.register(GuiScreen.class);
        mapper.register(ASMUtils.getMethod(GuiScreen.class, "onUpdate()V"), m -> {
            final Method method = m.getMappedMethod(ASMUtils.getMethod(Minecraft.class, "tick()V"));
            if(method != null)
            {
                MethodNode methodNode = ASMUtils.getMethodNode(method);
                assert methodNode != null;
                int offset = 0;
                for(int i = 0; i < methodNode.instructions.size(); i++)
                {
                    final AbstractInsnNode insnNode = methodNode.instructions.get(i);
                    if(insnNode.getType() == AbstractInsnNode.LDC_INSN &&
                       "Ticking screen".equals(((LdcInsnNode)insnNode).cst.toString()))
                    {
                        offset = i;
                        break;
                    }
                }
                final MethodInsnNode methodInsnNode = (MethodInsnNode)ASMUtils.getLastInstruction(methodNode.instructions, Opcodes.INVOKEVIRTUAL, methodNode.instructions.size() - offset);
                return ASMUtils.getMethod(m.getMappedClass(GuiScreen.class), methodInsnNode.name + methodInsnNode.desc);
            }
            return null;
        });
        mapper.register(ASMUtils.getMethod(GuiScreen.class, "onClose()V"), m -> {
            final Method method = m.getMappedMethod(ASMUtils.getMethod(Minecraft.class, "displayGuiScreen(L" + Type.getInternalName(GuiScreen.class) + ";)V"));
            if(method != null)
            {
                final MethodInsnNode methodInsnNode = (MethodInsnNode)ASMUtils.getFirst(ASMUtils.getMethodNode(method).instructions, Opcodes.INVOKEVIRTUAL);
                return ASMUtils.getMethod(m.getMappedClass(GuiScreen.class), methodInsnNode.name + methodInsnNode.desc);
            }
            return null;
        });
        mapper.register(NetHandler.class);
        mapper.register(GameSettings.class);
        mapper.register(GuiIngame.class);
        return mapper;
    }

    /**
     * @return an instance of the {@link Mapper} or if non is available it creates one.
     */
    public static Mapper getInstance()
    {
        return instance != null ? instance : (instance = createMapper());
    }

    /**
     * Registers a {@link Wrapper}.
     *
     * @param wrapper a the {@link Wrapper} which is supposed to be discovered.
     * @param discoveryHandlers {@link DiscoveryHandler} which could be used to
     *                          find the wrapper.
     *
     * @see #generate()
     */
    public void register(Class<? extends Wrapper> wrapper, DiscoveryHandler<? extends Class>... discoveryHandlers)
    {
        wrapperClasses.put(wrapper, Arrays.asList(discoveryHandlers));
    }

    /**
     * Unregisters a {@link Wrapper} which is supposed to be discovered.
     *
     * @param wrapper a the {@link Wrapper} which is supposed to be removed.
     */
    public void unregister(Class<? extends Wrapper> wrapper)
    {
        wrapperClasses.remove(wrapper);
    }

    /**
     * Attaches a custom {@link DiscoveryHandler} to a method.
     *
     * @param method the method for which the {@link DiscoveryHandler}s should be used.
     * @param discoveryHandlers the {@link DiscoveryHandler} which is used to find
     *                          the method.
     *
     * @see #generate()
     */
    public void register(Method method, DiscoveryHandler... discoveryHandlers)
    {
        customDiscoveryMethods.put(method, Arrays.asList(discoveryHandlers));
    }

    /**
     * Removes a custom {@link DiscoveryHandler}
     *
     * @param method the method whose {@link DiscoveryHandler} should be removed.
     */
    public void unregister(Method method)
    {
        customDiscoveryMethods.remove(method);
    }

    /**
     * <p>This method will generate mappings for fields methods and classes based on
     *    multiple factors including:</p>
     * <ul>
     *     <li>
     *         <b>Classes:</b>
     *         <ul>
     *             <li>Constructors</li>
     *             <li>StringConstants</li>
     *             <li>Extension Analysis</li>
     *         </ul>
     *     </li>
     *     <li>
     *         <b>Fields:</b>
     *         <ul>
     *             <li>Modifier/Type Analysis</li>
     *             <li>Structure Analysis</li>
     *         </ul>
     *     </li>
     *     <li>
     *         <b>Methods:</b>
     *         <ul>
     *             <li>Modifier/Return-Type/Parameter/Exceptions Analysis</li>
     *             <li>StringConstants</li>
     *             <li>Opcodes</li>
     *         </ul>
     *     </li>
     *     <li>
     *         <b>Custom:</b>
     *         <p>Any kind of check that you provide for it to find other classes
     *            based on.</p>
     *     </li>
     * </ul>
     * <p><i>Note: </i>This method should only be called once.</p>
     *
     * @throws MappingException if the function could not find one or more of the
     *                          registered {@link Wrapper}s it throws this exception.
     */
    public void generate() throws MappingException
    {
        final TimeHelper timer = new TimeHelper();
        success = false;
        LOGGER.log(Level.INFO, "Started generation of mappings.");
        final Set<Class<? extends Wrapper>> wrappers = new LinkedHashSet<>(wrapperClasses.keySet());
        outer: while(!wrappers.isEmpty())
        {
            final Iterator<Class<? extends Wrapper>> wrapperIterator = wrappers.iterator();
            while(wrapperIterator.hasNext())
            {
                final Class<? extends Wrapper> wrapper = wrapperIterator.next();
                if(!mappedClasses.containsKey(wrapper))
                {
                    final DiscoveryMethod discoveryMethod = wrapper.getDeclaredAnnotation(DiscoveryMethod.class);
                    final int flags = discoveryMethod != null ? discoveryMethod.checks() : DEFAULT;

                    //check if a invalid flag is attached to the class.
                    if((flags & FIELD) != 0 ||
                       (flags & STRUCTURE_START) != 0 ||
                       (flags & STRUCTURE_END) != 0 ||
                       (flags & CONSTRUCTOR) != 0)
                        LOGGER.log(Level.WARNING, "The wrappers class provided has a invalid flags attached to it: " + wrapper.getName() + ":" + Integer.toBinaryString(flags));

                    final Class declaringClass;
                    //check default flag.
                    if((flags & DEFAULT) != 0 &&
                       //all default checks need extra information provided by #DiscoveryMethod.
                       discoveryMethod != null &&
                       //check if the declaring class is valid.
                       discoveryMethod.declaring() != Wrapper.class &&
                       (declaringClass = getMappedClass(discoveryMethod.declaring())) != null)
                    {
                        //perform default checks.
                        if(Stream.of(wrapper.getDeclaredMethods())
                                //check if there is a method with a constructor flag present.
                                .anyMatch(method -> {
                                    final DiscoveryMethod discovery = method.getDeclaredAnnotation(DiscoveryMethod.class);
                                    return discovery != null &&
                                           (discovery.checks() & CONSTRUCTOR) != 0;
                                })) inner: {
                            //attempt to find class with the constructor.
                            Class result = null;
                            //get expected constructors parameters
                            final List<List<Class>> parameters = Stream.of(wrapper.getDeclaredMethods()).filter(m -> {
                                final DiscoveryMethod methodDiscoveryMethod = m.getDeclaredAnnotation(DiscoveryMethod.class);
                                return methodDiscoveryMethod != null && (methodDiscoveryMethod.checks() & CONSTRUCTOR) != 0;
                            }).map(method -> Stream.of(method.getParameterTypes())
                                        .map(this::convertToMappedClass)
                                        .collect(Collectors.toList()))
                                    .collect(Collectors.toList());

                            //math constructors of the fields with the provided ones.
                            for(Class clazz : Stream.of(declaringClass.getDeclaredFields())
                                    .map(Field::getType)
                                    .collect(Collectors.toList())) {
                                //get current constructor parameters.
                                final List<List<Class>> constructorsParameters = Stream.of(clazz.getConstructors())
                                        .map(constructor -> Arrays.asList(constructor.getParameterTypes()))
                                        .collect(Collectors.toList());

                                //check if all of the constructor parameters match.
                                if(parameters.stream().allMatch(constructorsParameters::contains))
                                {
                                    if((flags & LAST_MATCH) == 0 &&
                                       result != null)
                                        break inner;
                                    result = clazz;
                                    if((flags & FIRST_MATCH) != 0)
                                        break;
                                }
                            }

                            //check if a result was found.
                            if(result != null)
                            {
                                mappedClasses.put(wrapper, result);
                                continue outer;
                            }
                        }
                        else if((flags & STRING_CONST) != 0 &&
                                discoveryMethod.constants().length != 0) inner: {
                            Class result = null;
                            //go to declaring class and get all the fields.
                            for(Class clazz : Stream.of(declaringClass.getDeclaredFields())
                                    //map the fields to their type.
                                    .map(Field::getType)
                                    .collect(Collectors.toList()))
                                if(ASMUtils.containsString(clazz, discoveryMethod.constants()))
                                {
                                    if((flags & LAST_MATCH) == 0 &&
                                       result != null)
                                        break inner;
                                    result = clazz;
                                    if((flags & FIRST_MATCH) != 0)
                                        break;
                                }

                            if(result != null)
                            {
                                mappedClasses.put(wrapper, result);
                                continue outer;
                            }
                        }
                        else if((flags & EXTENSION) != 0 &&
                                declaringClass.getSuperclass() != null)
                        {
                            mappedClasses.put(wrapper, declaringClass.getSuperclass());
                            continue outer;
                        }
                    }

                    //check if the class has a custom flag attached to it.
                    if((flags & CUSTOM) != 0)
                        for(DiscoveryHandler<? extends Class> discoveryHandler : wrapperClasses.get(wrapper))
                            try
                            {
                                Class discovered = discoveryHandler.discover(this);
                                if(discovered != null)
                                {
                                    mappedClasses.put(wrapper, discovered);
                                    continue outer;
                                }
                            }
                            catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e)
                            {
                                e.printStackTrace();
                            }
                }
                else //class was found attempt to map all the methods.
                {
                    int offset = -1;
                    final List<Method> methods = new LinkedList<>(Arrays.asList(ASMUtils.getDeclaredMethodsInOrder(wrapper)));
                    final Iterator<Method> methodIterator = methods.iterator();
                    while(methodIterator.hasNext())
                    {
                        final Method method = methodIterator.next();
                        offset++;

                        final DiscoveryMethod discoveryMethod = method.getDeclaredAnnotation(DiscoveryMethod.class);
                        final int flags = discoveryMethod != null ? discoveryMethod.checks() : DEFAULT;

                        //check for invalid flags.
                        if(discoveryMethod != null &&
                           discoveryMethod.declaring() != Wrapper.class)
                            LOGGER.log(Level.WARNING, "The method provided has a invalid declaring tag attached to it: " + wrapper.getName() + "#" + method.getName());

                        //if the method is a constructor remove it.
                        if((flags & CONSTRUCTOR) != 0 ||
                           getMappedMethod(method) != null ||
                           getMappedField(method) != null)
                        {
                            methodIterator.remove();
                            continue;
                        }

                        //perform a default check.
                        if((flags & DEFAULT) != 0)
                            if((flags & FIELD) != 0) inner: {
                                //attempt to find the field.
                                List<Method> structure = null;
                                //check if field is part of a structure i.e. fields whose order is very unlikely to change.
                                if((flags & STRUCTURE_START) != 0)
                                {
                                    structure = new ArrayList<>();
                                    final Method wrapperMethods[] = ASMUtils.getDeclaredMethodsInOrder(wrapper);
                                    //find the remaining fields that are part of the structure.
                                    for(int i = offset; i < wrapperMethods.length; i++)
                                    {
                                        final Method wrapperMethod = wrapperMethods[i];
                                        structure.add(wrapperMethod);
                                        final DiscoveryMethod discovery = wrapperMethod.getDeclaredAnnotation(DiscoveryMethod.class);
                                        if(discovery != null &&
                                           (discovery.checks() & STRUCTURE_END) != 0)
                                            break;
                                    }
                                }

                                Field result = null;
                                int resultOffset = 0;
                                final Field fields[] = getMappedClass(wrapper).getDeclaredFields();
                                loop: for(int i = 0; i < fields.length; i++)
                                {
                                    final Field field = fields[i];
                                    //check field modifiers.
                                    if((discoveryMethod == null ||
                                       discoveryMethod.modifiers() == 0 ||
                                       field.getModifiers() == discoveryMethod.modifiers()) &&
                                       //check field type.
                                       field.getType() == convertToMappedClass(method.getReturnType() != void.class ?
                                               method.getReturnType() :
                                               method.getParameterTypes().length == 1 ?
                                               method.getParameterTypes()[0] :
                                               null))
                                    {
                                        //check if structure.
                                        if(structure != null)
                                        {
                                            if(i + structure.size() - 1 >= fields.length)
                                                break;

                                            //make sure the rest of the fields fit into the structure provided.
                                            for(int j = i; j < fields.length && j - i < structure.size(); j++)
                                            {
                                                DiscoveryMethod discovery;
                                                final Method structureMethod = structure.get(j - i);
                                                if(convertToMappedClass(structureMethod.getReturnType() != void.class ?
                                                        structureMethod.getReturnType() :
                                                        structureMethod.getParameterTypes().length == 1 ?
                                                        structureMethod.getParameterTypes()[0] :
                                                        null) != fields[j].getType() ||
                                                        ((discovery = structureMethod.getDeclaredAnnotation(DiscoveryMethod.class)) != null &&
                                                         discovery.modifiers() != 0 &&
                                                         discovery.modifiers() != fields[j].getModifiers()))
                                                    continue loop;
                                            }
                                        }

                                        if((flags & LAST_MATCH) == 0 &&
                                           result != null)
                                            break inner;
                                        result = field;
                                        resultOffset = i;
                                        if((flags & FIRST_MATCH) != 0)
                                            break;
                                    }
                                }

                                //check if the field/s where found.
                                if(result != null)
                                {
                                    //map fields.
                                    if(structure != null)
                                        for(int i = resultOffset; i < fields.length && i - resultOffset < structure.size(); i++)
                                            mappedFields.put(structure.get(i - resultOffset), fields[i]);
                                    else
                                        mappedFields.put(method, result);
                                    continue outer;
                                }
                            }
                            else if((flags & STRING_CONST) != 0)
                            {
                                //attempt to find method based on a string constant.
                                final Class obfClass = getMappedClass(wrapper);
                                final ClassNode classNode = ASMUtils.getClassNode(obfClass);
                                for(MethodNode methodNode : (List<MethodNode>) classNode.methods)
                                {
                                    final Iterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();
                                    while(iterator.hasNext())
                                    {
                                        final AbstractInsnNode insnNode = iterator.next();
                                        if(insnNode.getType() == AbstractInsnNode.LDC_INSN &&
                                           Stream.of(discoveryMethod.constants())
                                                   .anyMatch(s -> s.equals(((LdcInsnNode)insnNode).cst.toString())))
                                        {
                                            mappedMethods.put(method, ASMUtils.getMethod(methodNode, obfClass));
                                            continue outer;
                                        }
                                    }
                                }
                            }
                            else inner: {
                                //attempt to find the method using its return-type/mods/parameters.
                                Method result = null;

                                //get the methods parameters.
                                final List<Class> parameters = Stream.of(method.getParameterTypes())
                                        .map(this::convertToMappedClass)
                                        .collect(Collectors.toList());

                                for(Method obfMethod : getMappedClass(wrapper).getDeclaredMethods())
                                    //check method modifiers.
                                    if((discoveryMethod == null ||
                                        discoveryMethod.modifiers() == 0 ||
                                        obfMethod.getModifiers() == discoveryMethod.modifiers()) &&
                                            //check method return-types.
                                            (convertToMappedClass(method.getReturnType()) == obfMethod.getReturnType() &&
                                            //check method parameters.
                                            parameters.equals(Arrays.asList(obfMethod.getParameterTypes()))) &&
                                            //check exceptions.
                                            (method.getExceptionTypes().length != 0 ||
                                             Arrays.asList(method.getExceptionTypes()).equals(Arrays.asList(obfMethod.getExceptionTypes()))) &&
                                            //check opcodes.
                                            ((flags & OPCODES) == 0 ||
                                            (discoveryMethod.opcodes().length != 0 &&
                                             ASMUtils.hasInstructionMatch(ASMUtils.getMethodNode(obfMethod).instructions, discoveryMethod.opcodes()))))
                                    {
                                        //check if method is unique.
                                        if((flags & LAST_MATCH) == 0 &&
                                           result != null)
                                            break inner;
                                        result = obfMethod;
                                        if((flags & FIRST_MATCH) != 0)
                                            break;
                                    }

                                if(result != null)
                                {
                                    mappedMethods.put(method, result);
                                    continue outer;
                                }
                            }

                        //perform a custom check.
                        if((flags & CUSTOM) != 0)
                        {
                            final List<DiscoveryHandler> discoveryHandlers = customDiscoveryMethods.get(method);
                            if(discoveryHandlers != null)
                                for(DiscoveryHandler discoveryHandler : discoveryHandlers)
                                    try
                                    {
                                        Object discovered = discoveryHandler.discover(this);
                                        if(discovered != null)
                                        {
                                            if(discovered instanceof Method)
                                                mappedMethods.put(method, (Method) discovered);
                                            else if(discovered instanceof Field)
                                                mappedFields.put(method, (Field) discovered);
                                            else
                                                LOGGER.log(Level.WARNING, "A discovery method returned a invalid type." + wrapper);
                                            continue outer;
                                        }
                                    }
                                    catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e)
                                    {
                                        e.printStackTrace();
                                    }
                        }
                    }

                    if(methods.size() == 0)
                    {
                        LOGGER.log(Level.INFO, "Discovered class " + wrapper + ":" + mappedClasses.get(wrapper) + ".");
                        for(Method  method : wrapper.getDeclaredMethods())
                            LOGGER.log(Level.INFO, "  " + (mappedMethods.get(method) != null ?
                                    "Discovered method " + method.getName() + ":" + mappedMethods.get(method).getName() :
                                    "Discovered field " + method.getName() + ":" + mappedFields.get(method)));
                        wrapperIterator.remove();
                        continue outer;
                    }
                }
            }
            throw new MappingException("Could not generate mappings for " + wrappers.size() + " wrappers", wrappers);
        }
        LOGGER.log(Level.INFO, "Mappings were generated in: " + timer.getMSPassed() + "ms.");
        success = true;
    }

    /**
     * Helper method for {@link #generate()}.
     * Converts given class to a mapped one if it's a wrapper.
     *
     * @param clazz the class which should be converted.
     *
     * @see #generate()
     *
     * @return the mapped version of the class.
     */
    private Class convertToMappedClass(Class clazz)
    {
        return Wrapper.class.isAssignableFrom(clazz) ?
                getMappedClass((Class<? extends Wrapper>)clazz) :
                clazz;
    }

    /**
     * @param wrapper the {@link Wrapper} for which the mapped class should be returned.
     *
     * @see #generate()
     *
     * @return the class mapped to the {@link Wrapper}.
     */
    public Class getMappedClass(Class<? extends Wrapper> wrapper)
    {
        return mappedClasses.get(wrapper);
    }

    /**
     * @param method the method for which the mapped method should be returned.
     *
     * @see #generate()
     *
     * @return the method mapped to the method.
     */
    public Method getMappedMethod(Method method)
    {
        return mappedMethods.get(method);
    }

    /**
     * @param method the method for which the mapped field should be returned.
     *
     * @see #generate()
     *
     * @return the field mapped to the method.
     */
    public Field getMappedField(Method method)
    {
        return mappedFields.get(method);
    }

    /**
     * @return whether or not mappings were generated successfully.
     */
    public boolean isSuccess()
    {
        return success;
    }
}
