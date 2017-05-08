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

package com.nur1popcorn.irrlicht.engine.exceptions;

import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;

import java.util.Set;

/**
 * The {@link MappingException} is an exception thrown by the
 * {@link com.nur1popcorn.irrlicht.engine.mapper.Mapper}
 *
 * @see com.nur1popcorn.irrlicht.engine.mapper.Mapper
 * @see Wrapper
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class MappingException extends Exception
{
    private Set<Class<? extends Wrapper>> wrappers;

    public MappingException(String message, Set<Class<? extends Wrapper>> wrappers)
    {
        super(message);
        this.wrappers = wrappers;
    }

    /**
     * the {@link Wrapper}s for which the
     * {@link com.nur1popcorn.irrlicht.engine.mapper.Mapper} failed to generate mappings.
     *
     * @return the {@link Wrapper}s for which the generation failed.
     */
    public Set<Class<? extends Wrapper>> getWrappers()
    {
        return wrappers;
    }
}
