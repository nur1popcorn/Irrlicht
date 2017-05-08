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

import com.nur1popcorn.irrlicht.gui.GuiManager;
import com.nur1popcorn.irrlicht.gui.components.Button;
import com.nur1popcorn.irrlicht.gui.components.Checkbox;
import com.nur1popcorn.irrlicht.gui.components.Component;
import com.nur1popcorn.irrlicht.gui.components.Label;
import com.nur1popcorn.irrlicht.gui.components.*;
import com.nur1popcorn.irrlicht.gui.components.containers.BoxContainer;
import com.nur1popcorn.irrlicht.gui.components.containers.Container;
import com.nur1popcorn.irrlicht.gui.components.containers.Frame;
import com.nur1popcorn.irrlicht.gui.components.containers.ScrollContainer;
import com.nur1popcorn.irrlicht.gui.components.layout.Direction;
import com.nur1popcorn.irrlicht.gui.font.FontRenderer;
import com.nur1popcorn.irrlicht.gui.themes.parsing.Style;
import com.nur1popcorn.irrlicht.gui.themes.parsing.exceptions.ParserException;
import com.nur1popcorn.irrlicht.gui.themes.vis.*;
import com.nur1popcorn.irrlicht.utils.RenderUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The {@link Theme} is responsible handling and rendering {@link Component}s using
 * {@link ComponentHandler}s and custom {@link Style}s it is also responsible for storing
 * the {@link FontRenderer}s for the {@link ComponentHandler}s.
 *
 * @see Theme
 * @see Component
 * @see ComponentHandler
 * @see Style
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Theme
{
    private final String name;
    private Map<Class<? extends Component>, ComponentHandler> handlerMap = new HashMap<>();
    private Map<Font, FontRenderer> fontRendererMap = new HashMap<>();
    private List<Style> customStyles = new ArrayList<>();
    private GuiManager guiManager;
    private boolean success,
                    debug;

    public Theme(GuiManager guiManager, String name)
    {
        this.guiManager = guiManager;
        this.name = name;
    }

    /**
     * Factory method used to generate the "Vis" {@link Theme}.
     *
     * @param guiManager the {@link GuiManager} for which the {@link Theme} is created.
     *
     * @return the "Vis" {@link Theme}.
     */
    public static Theme createVisTheme(GuiManager guiManager)
    {
        final Theme theme = new Theme(guiManager, "Vis");
        theme.addHandler(new ContainerHandler(theme), Container.class, BoxContainer.class);
        theme.addHandler(new VisButtonHandler(theme), Button.class);
        theme.addHandler(new VisFrameHandler(theme), Frame.class);
        theme.addHandler(new VisLabelHandler(theme), Label.class);
        theme.addHandler(new VisTextBoxHandler(theme), TextBox.class);
        theme.addHandler(new VisScrollContainer(theme), ScrollContainer.class);
        theme.addHandler(new VisSliderHandler(theme), Slider.class);
        theme.addHandler(new VisCheckboxHandler(theme), Checkbox.class);
        return theme;
    }

    /**
     * Maps a {@link ComponentHandler} to a {@link Component} responsible for handling
     * input and drawing the {@link Component}.
     *
     * @param handler the handler responsible for handling input and drawing the
     *                {@link Component}.
     * @param components the type of {@link Component}s passed to the handler.
     *
     * @see #draw(Component, int, int, float)
     * @see #handleInput(Component, int, int, int, MouseEventType)
     */
    public void addHandler(ComponentHandler handler, Class<? extends Component>... components)
    {
        for(Class<? extends Component> clazz: components)
            handlerMap.put(clazz, handler);
    }

    /**
     * Adds a custom {@link Style} should be called before calling {@link #init()}.
     *
     * @see #addCustomStyle(List)
     */
    public void addCustomStyle(Style... styles)
    {
        addCustomStyle(Arrays.asList(styles));
    }

    /**
     * <p>
     *    Adds a custom {@link Style} should be called before calling {@link #init()} due
     *    to {@link #init()} function being responsible for merging the custom {@link Style}s.
     *    This can be used to override default variables.
     * </p>
     * <p>
     *     default example:
     * </p>
     * <pre>
     * .default {
     *    -color: #ffff0000;
     * }
     * </pre>
     * <p>
     *     The variable color can be overridden by defining another style like this:
     * </p>
     * <pre>
     * .default {
     *    -color: #ff0000ff;
     * }
     * </pre>
     */
    public void addCustomStyle(List<Style> styles)
    {
        for(Style style : styles)
            if(customStyles.contains(style))
                customStyles.stream().filter(customStyle -> customStyle.getName().equals(style.getName())).forEach(customStyle -> {
                    try
                    {
                        customStyle.merge(style);
                    }
                    catch (ParserException e)
                    {
                        e.printStackTrace();
                    }
                });
            else
                customStyles.add(style);
    }

    /**
     * @param name the name used to retrieve the custom {@link Style}.
     *
     * @return the custom {@link Style} with the name.
     */
    public Style getCustomStyle(String name)
    {
        for(Style style : customStyles)
            if(style.getName().equals(name))
                return style;
        return null;
    }

    /**
     * Removes a {@link Style} based on the name provided.
     *
     * @param name the name used to identify the {@link Style} which is supposed to
     *             be removed.
     */
    public void removeCustomStyle(String name)
    {
        customStyles.stream()
                .filter(style -> style.getName().equals(name))
                .forEach(style -> customStyles.remove(style));
    }

    /**
     * Removes all custom {@link Style}s.
     */
    public void clearCustomStyles()
    {
        customStyles.clear();
    }

    /**
     * Used to merge the custom {@link Style}s with default ones and load fonts if
     * needed.
     *
     * @see GuiManager
     */
    public void init() throws ParserException
    {
        success = false;
        for(Class clazz : handlerMap.keySet())
        {
            final ComponentHandler handler = handlerMap.get(clazz);
            final List<Style> styles = Style.parse(handler.getStyle());
            for(Style style : styles)
            {
                Style otherStyle = getCustomStyle(style.getName());
                if(otherStyle != null)
                    style.merge(otherStyle);
            }
            handler.loadStyles(styles);
            handler.init();
        }
        success = true;
    }

    /**
     * Used to clean up resources.
     *
     * @see GuiManager
     */
    public void free()
    {
        handlerMap.values().forEach(ComponentHandler::free);

        final Iterator<Font> iterator = fontRendererMap.keySet().iterator();
        while(iterator.hasNext())
        {
            fontRendererMap.get(iterator.next()).free();
            iterator.remove();
        }
    }

    /**
     * @return whether or not calling the {@link #init()} function was successful.
     */
    public boolean isSuccess()
    {
        return success;
    }

    /**
     * Sets whether or not the layout should be debugged.
     *
     * @param debug whether or not the layout should be debugged.
     *
     * @see #draw(Component, int, int, float)
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * @return whether or not the layout should be debugged.
     */
    public boolean isDebug()
    {
        return debug;
    }

    /**
     * Handles input for the {@link Component} passed.
     *
     * @param component the {@link Component} the action should be performed for.
     * @param mouseX the x position of the mouse.
     * @param mouseY the y position of the mouse.
     * @param mouseButton the mouse button clicked.
     * @param mouseEventType the type of mouse event.
     *
     * @see MouseEventType
     *
     * @return whether or not the input was detected.
     */
    public boolean handleInput(Component component, int mouseX, int mouseY, int mouseButton, MouseEventType mouseEventType)
    {
        ComponentHandler componentHandler;
        if(success && (componentHandler = handlerMap.get(component.getClass())) != null)
            return componentHandler.handleInput(component, mouseX, mouseY, mouseButton, mouseEventType);
        return false;
    }

    /**
     * Handles keyboard input for the {@link Component} passed.
     *
     * @param component the {@link Component} the action should be performed for.
     * @param typedChar the char which was typed.
     * @param keyCode the keycode of the key which was pressed.
     *
     * @return whether or not the input was detected.
     */
    public boolean handleKeyTyped(Component component, char typedChar, int keyCode)
    {
        ComponentHandler componentHandler;
        if(success && (componentHandler = handlerMap.get(component.getClass())) != null)
            return componentHandler.handleKeyTyped(component, typedChar, keyCode);
        return false;
    }


    /**
     * Handles drawing for the {@link Component} passed to the function.
     *
     * @param component the {@link Component} which should be drawn.
     * @param mouseX the x position of the mouse.
     * @param mouseY the y position of the mouse.
     * @param partialTicks the amount of time passed in between ticks.
     */
    public void draw(Component component, int mouseX, int mouseY, float partialTicks)
    {
        ComponentHandler componentHandler;
        if(success && (componentHandler = handlerMap.get(component.getClass())) != null)
        {
            componentHandler.draw(component, mouseX, mouseY, partialTicks);
            if(debug)
            {
                //margin
                RenderUtils.drawBorder(component.getX(), component.getY(), component.getFullWidthNeeded(), component.getFullHeightNeeded(), 0.3125f, 0xffff0000);
                //padding
                RenderUtils.drawBorder(
                        component.getX() + component.getMargin(Direction.LEFT),
                        component.getY() + component.getMargin(Direction.TOP),
                        component.getFullWidth(),
                        component.getFullHeight(),
                        0.3125f,
                        0xff00ff00);
                //size
                RenderUtils.drawBorder(
                        component.getX() + component.getMargin(Direction.LEFT) + component.getPadding(Direction.LEFT),
                        component.getY() + component.getMargin(Direction.TOP) + component.getPadding(Direction.TOP),
                        component.getWidth(),
                        component.getHeight(),
                        0.3125f,
                        0xff0000ff);
            }
        }
    }

    /**
     * Called every tick used to animate things 1 tick = 1 20th of a second.
     *
     * @param component the {@link Component} which should be updated.
     */
    public void update(Component component)
    {
        ComponentHandler componentHandler;
        if(success && (componentHandler = handlerMap.get(component.getClass())) != null)
            componentHandler.update(component);
    }

    /**
     * Returns a {@link FontRenderer} if it is present in the {@link Theme}
     * else it creates one.
     *
     * @param font the font used to either identify a already existing {@link FontRenderer} or
     *             create a new one.
     *
     * @return a {@link FontRenderer} based on it's on the font provided.
     */
    public FontRenderer getFontRenderer(Font font)
    {
        FontRenderer fontRenderer = fontRendererMap.get(font);
        if(fontRenderer != null)
            return fontRenderer;
        fontRendererMap.put(font, fontRenderer = new FontRenderer(font));
        return fontRenderer;
    }

    /**
     * @param font the font for which the check should be performed.
     *
     * @return whether or not a {@link FontRenderer} based on the font provided has already
     *         been created.
     */
    public boolean hasFont(Font font)
    {
        return fontRendererMap.containsKey(font);
    }

    /**
     * @return the name of the {@link Theme}.
     */
    public String getName()
    {
        return name;
    }


    /**
     * @return the {@link GuiManager} which stores the {@link Theme}.
     */
    public GuiManager getGuiManager()
    {
        return guiManager;
    }
}
