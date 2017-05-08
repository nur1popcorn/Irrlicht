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

package com.nur1popcorn.irrlicht.utils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The {@link ASMUtils} provides useful helper methods for working with bytecode.
 *
 * @see com.nur1popcorn.irrlicht.engine.hooker.Hooker
 * @see com.nur1popcorn.irrlicht.engine.mapper.Mapper
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class ASMUtils
{
    private static final Map<String, ClassNode> CLASS_NODE_CACHE = new HashMap<>();

    //prevent construction :/
    private ASMUtils()
    {}

    /**
     * @param clazz the class for which the {@link ClassNode} will be generated.
     *
     * @return creates and stores a {@link ClassNode} form the class provided.
     */
    public static ClassNode getClassNode(Class clazz)
    {
        try
        {
            return getClassNode(Type.getInternalName(clazz) + ".class");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param internalName the name used to find the {@link ClassNode}.
     *
     * @return creates and stores a {@link ClassNode} from the internal name provided.
     */
    public static ClassNode getClassNode(String internalName) throws IOException
    {
        if(CLASS_NODE_CACHE.containsKey(internalName))
            return CLASS_NODE_CACHE.get(internalName);
        final InputStream inputStream = ASMUtils.class.getResourceAsStream("/" + internalName);
        assert inputStream != null;
        final ClassNode classNode = getClassNode(IOUtils.readFully(inputStream, -1, true));
        CLASS_NODE_CACHE.put(internalName, classNode);
        return classNode;
    }

    /**
     * Creates a {@link ClassNode} from bytes.
     *
     * @param bytes the bytes used to create the {@link ClassNode}.
     *
     * @return the {@link ClassNode} from the bytes.
     */
    public static ClassNode getClassNode(byte[] bytes)
    {
        final ClassNode classNode = new ClassNode();
        new ClassReader(bytes).accept(classNode, ClassReader.SKIP_FRAMES);
        return classNode;
    }

    /**
     * @param classNode the {@link ClassNode} for which the bytes should be returned.
     *
     * @return the bytes of a {@link ClassNode}.
     */
    public static byte[] getBytes(ClassNode classNode)
    {
        final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    /**
     * @param insnList the list of instructions.
     * @param opcode the opcode used to identify the instruction.
     *
     * @return the last instruction from the list with the opcode provided.
     */
    public static AbstractInsnNode getLastInstruction(InsnList insnList, int opcode)
    {
        return getLastInstruction(insnList, opcode, 0);
    }

    /**
     * @param insnList the list of instructions.
     * @param opcode the opcode used to identify the instruction.
     * @param offset the offset from which the search is started.
     *
     * @return the last instruction from the list from the offset with the opcode provided.
     */
    public static AbstractInsnNode getLastInstruction(InsnList insnList, int opcode, int offset)
    {
        for(int i = insnList.size() - 1 - offset; i > 0; i--)
        {
            final AbstractInsnNode abstractInsnNode = insnList.get(i);
            if (abstractInsnNode.getOpcode() == opcode)
                return abstractInsnNode;
        }
        return null;
    }

    /**
     * @param method the method for which the {@link MethodNode} should be returned.
     *
     * @return the {@link MethodNode} for the provided method.
     */
    public static MethodNode getMethodNode(Method method)
    {
        for(MethodNode methodNode : (List<MethodNode>) ASMUtils.getClassNode(method.getDeclaringClass()).methods)
            if(methodNode.name.equals(method.getName()) && methodNode.desc.equals(Type.getMethodDescriptor(method)))
                return methodNode;
        return null;
    }

    /**
     * @param clazz the class in which the method is located.
     * @param desc the method's description and name.
     *
     * @return the method in the class with the description.
     */
    public static Method getMethod(Class clazz, String desc)
    {
        for(Method method : clazz.getDeclaredMethods())
            if((method.getName() + Type.getMethodDescriptor(method)).equals(desc))
                return method;
        return null;
    }

    /**
     * @param clazz the class which should be checked.
     * @param constants the string constants for which should be checked whether or not
     *                  they are contained by the class.
     *
     * @return whether or not the class contains all of the string constants.
     */
    public static boolean containsString(Class clazz, String constants[])
    {
        if(clazz.isPrimitive() ||
           clazz.isArray())
            return false;

        ClassNode classNode = getClassNode(clazz);
        return ((List<MethodNode>)classNode.methods).stream()
                .anyMatch(methodNode -> {
                    final Iterator iterator = methodNode.instructions.iterator();
                    while(iterator.hasNext())
                    {
                        final AbstractInsnNode insnNode = (AbstractInsnNode) iterator.next();
                        if(insnNode.getType() == AbstractInsnNode.LDC_INSN &&
                           Stream.of(constants).anyMatch(s -> s.equals(((LdcInsnNode)insnNode).cst.toString())))
                            return true;
                    }
                    return false;
                });
    }

    /**
     * @param clazz the class for which the methods should be returned.
     *
     * @return the methods in the class in the order they are declared
     *         since Class#getDeclaredMethods returns them in a not specified order.
     */
    public static Method[] getDeclaredMethodsInOrder(Class clazz)
    {
        final Method methods[] = clazz.getDeclaredMethods();
        final ClassNode classNode = getClassNode(clazz);
        final Method reordered[] = new Method[methods.length];
        for(int i = 0; i < classNode.methods.size(); i++)
        {
            final MethodNode methodNode = (MethodNode) classNode.methods.get(i);
            for(Method method : methods)
                if((method.getName() + Type.getMethodDescriptor(method)).equals(methodNode.name + methodNode.desc))
                {
                    reordered[i] = method;
                    break;
                }
        }
        return reordered;
    }

    /**
     * @param methodNode the {@link MethodNode} for which the method should be obtained.
     * @param clazz the class in which the method is located.
     *
     * @return a method based on the {@link MethodNode} and class provided.
     */
    public static Method getMethod(MethodNode methodNode, Class clazz)
    {
        for(Method method : clazz.getDeclaredMethods())
            if((method.getName() + Type.getMethodDescriptor(method)).equals(methodNode.name + methodNode.desc))
                return method;
        return null;
    }

    /**
     * @param insnList the list which should be searched.
     * @param opcode the opcode for which should be searched.
     *
     * @return the first instruction with the opcode provided inside of the list.
     */
    public static AbstractInsnNode getFirst(InsnList insnList, int opcode)
    {
        final Iterator<AbstractInsnNode> iterator = insnList.iterator();
        while(iterator.hasNext())
        {
            final AbstractInsnNode insnNode = iterator.next();
            if(insnNode.getOpcode() == opcode)
                return insnNode;
        }
        return null;
    }

    /**
     * @param insnList the list which should be searched.
     * @param opcode the opcode for which should be searched.
     *
     * @return the last instruction with the opcode provided inside of the list.
     */
    public static AbstractInsnNode getLast(InsnList insnList, int opcode)
    {
        for(int i = insnList.size() - 1; i > 0; i--)
        {
            final AbstractInsnNode abstractInsnNode = insnList.get(i);
            if(abstractInsnNode.getOpcode() == opcode)
                return abstractInsnNode;
        }
        return null;
    }

    /**
     * Inserts a list of instructions after or before a unique set of opcodes in the list provided.
     *
     * @param insnList the list to which the instructions should be added.
     * @param opcodes the unique set of opcodes.
     * @param injection the instructions which should be added.
     * @param before whether the instructions should be added to end or not.
     */
    public static void insert(InsnList insnList, int opcodes[], InsnList injection, boolean before)
    {
        final AbstractInsnNode instructions[] = insnList.toArray();
        for(int i = 0; i < instructions.length; i++)
        {
            final AbstractInsnNode abstractInsnNode = instructions[i];
            if(checkInstructionMatch(insnList, opcodes, i))
            {
                if(before)
                    insnList.insertBefore(abstractInsnNode, injection);
                else
                    insnList.insert(instructions[i + opcodes.length - 1], injection);
                break;
            }
        }
    }

    /**
     * @param insnList the list of instructions which should be checked.
     * @param opcodes the unique set of opcodes.
     * @param offset the offset at which should be checked.
     *
     * @return whether or not there is an opcode match at the offset provided.
     */
    public static boolean checkInstructionMatch(InsnList insnList, int opcodes[], int offset)
    {
        final AbstractInsnNode instructions[] = insnList.toArray();
        int i = offset;
        for(; i < instructions.length && i - offset < opcodes.length; i++)
            if(instructions[i].getOpcode() != opcodes[i - offset])
                return false;
        return i - offset == opcodes.length;
    }

    /**
     * @param insnList the list of instructions which should be checked.
     * @param opcodes the opcodes used to check the list of instructions.
     *
     * @return whether or not there is a opcode match at all.
     */
    public static boolean hasInstructionMatch(InsnList insnList, int opcodes[])
    {
        final AbstractInsnNode instructions[] = insnList.toArray();
        for(int i = 0; i < instructions.length; i++)
            if(checkInstructionMatch(insnList, opcodes, i))
                return true;
        return false;
    }

    /**
     * @param methodNode the {@link MethodNode} which should be formatted.
     *
     * @return a formatted version of the instructions in the {@link MethodNode}.
     */
    public static String formatInstructions(MethodNode methodNode)
    {
        final Printer printer = new Textifier();
        final TraceMethodVisitor traceMethodVisitor = new TraceMethodVisitor(printer);
        final StringBuilder stringBuilder = new StringBuilder();
        final Iterator<AbstractInsnNode> insnNodeIterator = methodNode.instructions.iterator();
        while(insnNodeIterator.hasNext())
        {
            final AbstractInsnNode insnNode = insnNodeIterator.next();
            insnNode.accept(traceMethodVisitor);
            final StringWriter stringWriter = new StringWriter();
            printer.print(new PrintWriter(stringWriter));
            printer.getText().clear();
            stringBuilder.append(stringWriter.toString());
        }
        return stringBuilder.toString();
    }
}
