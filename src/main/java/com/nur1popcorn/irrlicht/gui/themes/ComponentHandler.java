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

package com.nur1popcorn.irrlicht.gui.themes;

import com.nur1popcorn.irrlicht.gui.components.Component;
import com.nur1popcorn.irrlicht.gui.components.layout.Orientation;
import com.nur1popcorn.irrlicht.gui.themes.parsing.Style;

import java.util.List;

/**
 * The {@link ComponentHandler}s are used to draw and handle mouse input for {@link Component}s.
 *
 * @see Style
 * @see Theme
 * @see Component
 * @see ComponentHandler
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public abstract class ComponentHandler<T extends Component>
{
    protected final Theme theme;
    protected List<Style> styles;

    //prevent construction :/
    protected ComponentHandler(Theme theme)
    {
        this.theme = theme;
    }

    /**
     * Loads {@link Style}s.
     *
     * @param styles a list of {@link Style}s.
     *
     * @see Style
     * @see #handleInput(Component, int, int, int, MouseEventType)
     * @see #draw(Component, int, int, float)
     */
    public final void loadStyles(List<Style> styles)
    {
        this.styles = styles;
    }

    /**
     * @param name the name used to retrieve the {@link Style}.
     *
     * @return the {@link Style} with the name.
     */
    public final Style getStyle(String name)
    {
        for(Style style : styles)
            if(style.getName().equals(name))
                return style;
        return null;
    }

    /**
     * Called once after the {@link Style}s were assigned. Should be used to load
     * things like fonts based on the {@link Style}.
     *
     * @see Theme
     */
    public void init()
    {}

    /**
     * Called once by the {@link Theme} used to free resources like fonts.
     */
    public void free()
    {}

    /**
     * Used for handling mouse clicks.
     *
     * @param component the component the action should be performed for.
     * @param mouseX the x position of the mouse.
     * @param mouseY the y position of the mouse.
     * @param mouseButton the mouse button clicked.
     * @param mouseEventType the type of mouse event.
     *
     * @see MouseEventType
     *
     * @return whether or not the input was detected.
     */
    public boolean handleInput(T component, int mouseX, int mouseY, int mouseButton, MouseEventType mouseEventType)
    {
        return false;
    }

    /**
     * Handles keyboard input for the {@link Component} passed.
     *
     * @param component the {@link Component} the action should be performed for.
     * @param typedChar the char which was typed.
     * @param keyCode the keycode of the key that was typed.
     *
     * @return whether or not the input was detected.
     */
    public boolean handleKeyTyped(T component, char typedChar, int keyCode)
    {
        return false;
    }

    /**
     * Used for drawing {@link Component}s.
     *
     * @param component the {@link Component} which should be drawn.
     * @param mouseX the x position of the mouse.
     * @param mouseY the y position of the mouse.
     * @param partialTicks the amount of time passed in between ticks.
     */
    public void draw(T component, int mouseX, int mouseY, float partialTicks)
    {}

    /**
     * Called every tick used to animate things 1 tick = 1 20th of a second.
     *
     * @param component the {@link Component} which should be updated.
     */
    public void update(T component)
    {}

    /**
     * @see Style#parse(String)
     *
     * @return the {@link ComponentHandler}s base style as a string this can later
     *         be overridden.
     */
    public String getStyle()
    {
        return "";
    }

    /**
     * @param orientation the orientation used to determine whether to return the
     *                    width or height.
     *
     * @see #getPositonOffset(Component, float[])
     *
     * @return the width / height of the component based on the orientation provided.
     */
    private static float getSize(Orientation orientation, Component component)
    {
        return orientation == Orientation.VERTICAL ? component.getHeight() : component.getWidth();
    }

    /**
     * @param component the orientation used to distinguish between vertical and
     *                  horizontal size.
     * @param requiredSize the width and height needed to determine the offset.
     *
     * @return the base offset for each align based on the {@link Component} provided
     *         and total size needed.
     */
    public static float[] getPositonOffset(Component component, float requiredSize[])
    {
        float offset[] = new float[Orientation.values().length];
        for(Orientation orientation : Orientation.values())
            switch(component.getAlign()[orientation.ordinal()])
            {
                default:
                case LEFT:
                    offset[orientation.ordinal()] = 0;
                    break;
                case CENTER:
                    offset[orientation.ordinal()] = (getSize(orientation, component) - requiredSize[orientation.ordinal()]) / 2;
                    break;
                case RIGHT:
                    offset[orientation.ordinal()] = getSize(orientation, component) - requiredSize[orientation.ordinal()];
                    break;
            }
        return offset;
    }
}
