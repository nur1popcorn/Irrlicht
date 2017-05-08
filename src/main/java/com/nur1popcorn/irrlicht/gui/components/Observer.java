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

package com.nur1popcorn.irrlicht.gui.components;

/**
 * The {@link Observer}s are used to observe {@link Observable}s.
 *
 * @see Observable
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public interface Observer<T>
{
    /**
     * Called when the {@link Observable} the {@link Observer} is attached to changes the
     * value.
     *
     * @param observable the {@link Observable} the {@link Observer} is attached to.
     */
    public void onUpdate(Observable<T> observable);
}
