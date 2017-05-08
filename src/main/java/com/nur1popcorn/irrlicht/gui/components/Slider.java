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

import com.nur1popcorn.irrlicht.gui.components.layout.Orientation;
import com.nur1popcorn.irrlicht.management.values.SliderValue;

/**
 * The {@link Slider}s allow the one to choose a value in range from min to max at a
 * set increment.
 *
 * @see LockableComponent
 * @see Focusable
 * @see Observer
 * @see Observable
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Slider<T extends Number> extends LockableComponent implements Focusable
{
    private Orientation orientation;
    private Observable<T> value;
    private T min,
              max,
              increment;
    private boolean dragging;

    private final Class<? extends Number> numberType;

    public Slider(Orientation orientation, int width, int height, T min, T max, T value, T increment)
    {
        super(width, height);
        this.orientation = orientation;
        this.min = min;
        this.max = max;
        this.value = new Observable<>(value);
        this.increment = increment;
        numberType = value.getClass();
    }

    public Slider(Orientation orientation, T min, T max, T value, T increment)
    {
        this(orientation, orientation == Orientation.VERTICAL ? 5 : 32, orientation == Orientation.VERTICAL ? 32 : 5, min, max, value, increment);
    }

    public Slider(SliderValue sliderValue)
    {
        this(Orientation.HORIZONTAL, (T)sliderValue.min, (T)sliderValue.max, (T)sliderValue.value, (T)sliderValue.increment);
        onChange(observable -> sliderValue.value = value.get());
    }

    /**
     * @return the min value of the {@link Slider}.
     */
    public T getMin()
    {
        return min;
    }

    /**
     * Sets the min value of the {@link Slider}.
     *
     * @param min the min value of the {@link Slider}.
     */
    public void setMin(T min)
    {
        this.min = min;
    }

    /**
     * @return the max value of the {@link Slider}.
     */
    public T getMax()
    {
        return max;
    }

    /**
     * Sets the max value of the {@link Slider}.
     *
     * @param max the max value of the {@link Slider}.
     */
    public void setMax(T max)
    {
        this.max = max;
    }

    /**
     * @return the value of the {@link Slider}.
     */
    public T getValue()
    {
        return value.get();
    }

    /**
     * Sets the value of the {@link Slider}.
     *
     * @param value the value of the {@link Slider}.
     */
    public void setValue(T value)
    {
        this.value.set(value);
    }

    /**
     * Adds a {@link Observer} to the {@link Slider}'s value.
     *
     * @param observer the {@link Observer} used to observe the value.
     */
    public void onChange(Observer<T> observer)
    {
        value.register(observer);
    }

    /**
     * @return the increment of the {@link Slider}.
     */
    public T getIncrement()
    {
        return increment;
    }

    /**
     * Sets the increment of the {@link Slider}.
     *
     * @param increment the increment of the {@link Slider}.
     */
    public void setIncrement(T increment)
    {
        this.increment = increment;
    }

    /**
     * @return whether or not the {@link Component} is being dragged.
     */
    public boolean isDragging()
    {
        return dragging;
    }

    /**
     * Sets whether or not the {@link Component} is being dragged.
     *
     * @param dragging whether or not the {@link Component} is being dragged.
     */
    public void setDragging(boolean dragging)
    {
        this.dragging = dragging;
    }

    /**
     * @return the {@link Orientation} of the {@link Slider}.
     */
    public Orientation getOrientation()
    {
        return orientation;
    }

    /**
     * @return the type of {@link Number} the {@link Slider} uses.
     */
    public Class<? extends Number> getNumberType()
    {
        return numberType;
    }
}
