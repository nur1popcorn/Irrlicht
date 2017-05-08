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

/**
 * The {@link DiscoveryHandler} is responsible for handling custom checks.
 *
 * @see Mapper
 * @see Mapper#register(Class, DiscoveryHandler[])
 * @see com.nur1popcorn.irrlicht.engine.wrappers.Wrapper
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public interface DiscoveryHandler<T>
{
    /**
     * Finds the class/method/field it is attached to within the obfuscated classes.
     *
     * @param mapper the {@link Mapper} for which the class/method/field is supposed to be discovered.
     *
     * @throws ClassNotFoundException if the class wasn't found.
     * @throws NoSuchMethodException if the method wasn't found.
     * @throws NoSuchFieldException if the field wasn't found.
     *
     * @return the discovered class/method/field.
     */
    public T discover(Mapper mapper) throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException;
}