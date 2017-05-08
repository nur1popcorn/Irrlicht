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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link DiscoveryMethod} is responsible for telling the {@link Mapper} how a class/method/field
 * is supposed to be discovered. It does this by attaching additional information and the kinds of methods
 * that are supposed to be used to discover it.
 *
 * @see Wrapper
 * @see Mapper
 * @see DiscoveryHandler
 * @see WrapperDelegationHandler
 * @see java.lang.reflect.Modifier
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@Target({
    ElementType.TYPE,
    ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface DiscoveryMethod
{
    /**
     * The methods which are supposed to be used to discover the obfuscated class/method/field.
     *
     * @see Mapper
     *
     * @return the different kind of flags used to discover the class/method/field.
     */
    public int checks() default Mapper.DEFAULT;

    /**
     * @see Mapper
     * @see java.lang.reflect.Modifier
     *
     * @return The kind of modifiers attached to the method/field
     */
    public int modifiers() default 0;

    /**
     * The class declaring the class by extending it or declaring it as a field.
     *
     * <p>
     *    if the class declaring specified is {@link Wrapper} it will be ignored
     *    otherwise the interface specified should extend {@link Wrapper}
     * </p>
     *
     * @see Mapper
     *
     * @return the class declaring the class this is attached to.
     */
    public Class<? extends Wrapper> declaring() default Wrapper.class;

    /**
     * A set of class/method specific string constants.
     * Example:
     * minecraft:container
     *
     * @return a set of for the class/method unique string constants.
     */
    public String[] constants() default { };

    /**
     * A set of method specific opcodes:
     * Example:
     *
     * <pre>
     * aload0
     * getfield bew s
     * dconst_0
     * aload0
     * getfield bew u
     * invokespecial cj <init>((DDD)V);
     * invokevirtual adm e((Lcj;)Z);
     * ifne L3
     * </pre>
     *
     * @return a set of for the method unique opcodes.
     */
    public int[] opcodes() default { };
}
