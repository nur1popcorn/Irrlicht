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

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Observable}s are used to store {@link Observer}s and update them on value
 * change.
 *
 * @see Observer
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Observable<T>
{
    private List<Observer<T>> observers = new ArrayList<>();
    private T value;

    public Observable()
    {}

    public Observable(T value)
    {
        this.value = value;
    }

    /**
     * @return the value that is being observed.
     */
    public T get()
    {
        return value;
    }

    /**
     * Sets the value which is being observed and updates all of the {@link Observer}s.
     *
     * @param value the value is going to be set to.
     */
    public void set(T value)
    {
        this.value = value;
        observers.forEach(observer -> observer.onUpdate(this));
    }

    /**
     * Adds a {@link Observer}.
     *
     * @param observer the {@link Observer} to be added.
     */
    public void register(Observer<T> observer)
    {
        observers.add(observer);
    }

    /**
     * Removes a {@link Observer}.
     *
     * @param observer the {@link Observer} to be removed.
     */
    public void unregister(Observer<T> observer)
    {
        observers.remove(observer);
    }

    /***
     * Removes all of the {@link Observer}s.
     */
    public void clear()
    {
        observers.clear();
    }
}
