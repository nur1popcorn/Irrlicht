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

package com.nur1popcorn.irrlicht.engine.hooker.impl;

import com.nur1popcorn.irrlicht.engine.events.Event;
import org.lwjgl.opengl.Display;

/**
 * The {@link SwapBuffersEvent} is called when {@link Display#swapBuffers()} is called
 * and is used for performance measurements.
 *
 * @see Event
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
public class SwapBuffersEvent implements Event
{}
