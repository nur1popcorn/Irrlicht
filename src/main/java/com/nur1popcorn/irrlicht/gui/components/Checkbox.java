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

import com.nur1popcorn.irrlicht.management.values.ToggleValue;

/**
 * The {@link Checkbox} is used to toggle a value.
 *
 * @see LockableComponent
 * @see Focusable
 * @see Observable
 * @see Observer
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Checkbox extends LockableComponent implements Focusable
{
    private Observable<Boolean> checked;

    public Checkbox(boolean checked)
    {
        super(5, 5);
        this.checked = new Observable<>(checked);
    }

    public Checkbox()
    {
        this(false);
    }

    public Checkbox(ToggleValue toggleValue)
    {
        this(toggleValue.value);
        checked.register(observable -> toggleValue.value = observable.get());
    }

    /**
     * @return whether or not the {@link Checkbox} is checked.
     */
    public boolean isChecked()
    {
        return checked.get();
    }

    /**
     * Sets whether or not the {@link Checkbox} is checked.
     *
     * @param checked whether or not the {@link Checkbox} is checked.
     */
    public void setChecked(boolean checked)
    {
        this.checked.set(checked);
    }

    /**
     * Adds a {@link Observer} to the {@link Checkbox}'s value.
     *
     * @param observer the {@link Observer} used to observe the value.
     */
    public void onChange(Observer<Boolean> observer)
    {
        checked.register(observer);
    }
}
