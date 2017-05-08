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

package com.nur1popcorn.irrlicht.management.values;

import java.lang.annotation.*;

/**
 * The {@link ValueTarget} is used by the {@link com.nur1popcorn.irrlicht.modules.Module} class
 * to determine whether or not a {@link Value} is a setting.
 *
 * @see Value
 * @see com.nur1popcorn.irrlicht.modules.Module
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueTarget
{}
