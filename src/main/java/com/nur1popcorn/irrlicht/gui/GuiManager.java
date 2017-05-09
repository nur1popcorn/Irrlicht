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

package com.nur1popcorn.irrlicht.gui;

import com.nur1popcorn.irrlicht.Irrlicht;
import com.nur1popcorn.irrlicht.engine.events.EventManager;
import com.nur1popcorn.irrlicht.engine.events.EventTarget;
import com.nur1popcorn.irrlicht.engine.hooker.impl.UpdateEvent;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.mapper.WrapperDelegationHandler;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.client.Minecraft;
import com.nur1popcorn.irrlicht.engine.wrappers.client.gui.GuiScreen;
import com.nur1popcorn.irrlicht.gui.components.*;
import com.nur1popcorn.irrlicht.gui.components.builders.BoxBuilder;
import com.nur1popcorn.irrlicht.gui.components.containers.Container;
import com.nur1popcorn.irrlicht.gui.components.containers.Frame;
import com.nur1popcorn.irrlicht.gui.components.containers.ScrollContainer;
import com.nur1popcorn.irrlicht.gui.components.layout.Direction;
import com.nur1popcorn.irrlicht.gui.themes.MouseEventType;
import com.nur1popcorn.irrlicht.gui.themes.Theme;
import com.nur1popcorn.irrlicht.gui.themes.parsing.exceptions.ParserException;
import com.nur1popcorn.irrlicht.management.values.SliderValue;
import com.nur1popcorn.irrlicht.management.values.ToggleValue;
import com.nur1popcorn.irrlicht.modules.Category;
import javassist.util.proxy.ProxyFactory;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * The {@link GuiManager} is responsible for storing all of the {@link Component}s.
 * It also stores the current {@link Theme} which is used to render and handle all of
 * the {@link Component}s. On top of that it is also responsible for creating a {@link GuiScreen}
 * which acts as a bridge to the {@link GuiManager}.
 *
 *                   +----------+
 *               +---+GuiManager+---+
 *               |   +----------+   |
 *            +--v--+          +----v-----+
 *       +----+Theme<-----+----+Components|
 *       |    +--+--+     |    +----------+
 *  +----v-+ +---v----+ +-v---+
 *  |Styles+->Handlers<-+Fonts|
 *  +------+ +---+----+ +-----+
 *            +--v---+
 *            |Output|
 *            +------+
 *
 * @see Theme
 * @see Component
 * @see Container
 * @see GuiScreen
 * @see ProxyFactory
 * @see UpdateEvent
 * @see MouseEventType
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class GuiManager
{
    private final Container stage = new Container();
    private Component focused;
    private Theme theme;

    //prevent construction :/
    protected GuiManager()
    {
        EventManager.register(this);
    }

    /**
     * @return a proxy for a {@link GuiScreen} which acts as a bridge to the {@link GuiManager}.
     */
    public GuiScreen createGuiScreen()
    {
        try
        {
            final ProxyFactory proxyFactory = new ProxyFactory();
            final Mapper mapper = Mapper.getInstance();
            proxyFactory.setSuperclass(mapper.getMappedClass(GuiScreen.class));

            final GuiScreen guiScreen = new GuiScreen() {
                private int width,
                            height;

                @Override
                public void drawScreen(int mouseX, int mouseY, float partialTicks)
                {
                    draw(mouseX, mouseY, partialTicks);
                }

                @Override
                public void handleInput()
                {
                    if(Mouse.isCreated())
                        while(Mouse.next())
                        {
                            final int x = Mouse.getEventX() * width / Display.getWidth(),
                                      y = height - Mouse.getEventY() * height / Display.getHeight() - 1,
                                      button = Mouse.getEventButton();
                            if(Mouse.getEventButtonState())
                                mouseClicked(x, y, button);
                            else if(button != -1)
                                mouseReleased(x, y, button);
                        }

                    if(Keyboard.isCreated())
                        while(Keyboard.next())
                            if(Keyboard.getEventKeyState())
                                keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
                }

                @Override
                public void onUpdate()
                {
                    update();
                }

                @Override
                public void onClose()
                {
                    leave();
                }

                @Override
                public void initGui(Minecraft minecraft, int width, int height)
                {
                    Keyboard.enableRepeatEvents(true);
                    this.width = width;
                    this.height = height;
                    init();
                }

                @Override
                public void resize(Minecraft minecraft, int width, int height)
                {
                    initGui(minecraft, width, height);
                }

                @Override
                public boolean shouldPauseGame()
                {
                    return false;
                }

                @Override
                public Object getHandle()
                {
                    return this;
                }
            };

            return WrapperDelegationHandler.createWrapperProxy(GuiScreen.class, proxyFactory.create(null, null, (Object proxy, Method thisMethod, Method proceed, Object[] args) -> {
                for(Method method : GuiScreen.class.getDeclaredMethods())
                    if(mapper.getMappedMethod(method).equals(thisMethod))
                    {
                        final Class types[] = method.getParameterTypes();
                        final Object handledArgs[] = new Object[args.length];
                        for(int i = 0; i < args.length; i++)
                            handledArgs[i] = Wrapper.class.isAssignableFrom(types[i]) ?
                                    WrapperDelegationHandler.createWrapperProxy((Class<? extends Wrapper>)types[i], args[i]) :
                                    args[i];
                        return method.invoke(guiScreen, handledArgs);
                    }
                return null;
            }));
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A factory method used to create a default {@link GuiManager}.
     *
     * @return a default {@link GuiManager}.
     */
    public static GuiManager createGuiManager()
    {
        final GuiManager guiManager = new GuiManager();
        for(Category category : Category.values())
        {
            final BoxBuilder frameBoxBuilder = new BoxBuilder();
            Irrlicht.getIrrlicht().getModuleManager()
                    .getModulesByCategory(category)
                    .forEach(module -> {
                        frameBoxBuilder.setMargin(Direction.TOP, 1);
                        {
                            final Label name = new Label(module.getName());
                            name.setFitWidth(true);
                            frameBoxBuilder.append(name);
                        }

                        {
                            final Checkbox checkbox = new Checkbox();
                            checkbox.setMargin(Direction.RIGHT, 1);
                            checkbox.setChecked(module.isToggled());
                            checkbox.onChange(observable -> module.toggle());
                            frameBoxBuilder.append(checkbox);
                        }

                        module.getValues().forEach(value -> {
                            frameBoxBuilder.row();
                            for(Direction direction : new Direction[]
                                    {
                                        Direction.TOP,
                                        Direction.BOTTOM
                                    }
                                )
                            {
                                frameBoxBuilder.setMargin(direction, 2);
                            }

                            final Label desc = new Label(value.getName() + ": ");
                            desc.setFitWidth(true);
                            frameBoxBuilder.append(desc);
                            if(value instanceof SliderValue)
                                frameBoxBuilder.append(new Slider<>((SliderValue) value));
                            else if(value instanceof ToggleValue)
                            {
                                final Checkbox checkbox = new Checkbox((ToggleValue) value);
                                checkbox.setMargin(Direction.RIGHT, 1);
                                frameBoxBuilder.append(checkbox);
                            }
                        });
                        frameBoxBuilder.setMargin(Direction.BOTTOM, 5);
                        frameBoxBuilder.row();
                    });
            guiManager.getStage().add(new Frame(category.toString(), 50, 50, 75, 50, new ScrollContainer(frameBoxBuilder.build())));
        }
        return guiManager;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event)
    {
        final Minecraft minecraft = Irrlicht.getMinecraft();
        if(minecraft.getGuiScreen().getHandle() == null &&
           Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
           minecraft.displayGuiScreen(createGuiScreen());
    }

    /**
     * Draws all the {@link Component}s using the provided {@link Theme}.
     *
     * @param mouseX the x position of the mouse.
     * @param mouseY the y position of the mouse.
     * @param partialTicks the amount of time passed in between ticks.
     *
     * @see GuiScreen
     */
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        theme.draw(stage, mouseX, mouseY, partialTicks);
    }

    /**
     * Called when any mouse button is clicked.
     *
     * @param mouseX the x position of the mouse.
     * @param mouseY the y position of the mouse.
     * @param mouseButton the mouse button clicked.
     *
     * @see GuiScreen
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        handleMouseInput(mouseX, mouseY, mouseButton, MouseEventType.PRESSED);
    }

    /**
     * Called when any mouse button is released.
     *
     * @param mouseX the x position of the mouse.
     * @param mouseY the y position of the mouse.
     * @param mouseButton the mouse button released.
     *
     * @see GuiScreen
     */
    public void mouseReleased(int mouseX, int mouseY, int mouseButton)
    {
        handleMouseInput(mouseX, mouseY, mouseButton, MouseEventType.RELEASED);
    }

    /**
     * Calls the {@link Theme} handleInput function for every component in the stage.
     *
     * @param mouseX the x position of the mouse.
     * @param mouseY the y position of the mouse.
     * @param mouseButton the mouse button released.
     * @param mouseEventType the type of mouse event.
     *
     * @see #mouseClicked(int, int, int)
     * @see #mouseReleased(int, int, int)
     * @see MouseEventType
     */
    private void handleMouseInput(int mouseX, int mouseY, int mouseButton, MouseEventType mouseEventType)
    {
        final List<Component> components = stage.getComponents();
        for(int i = components.size() - 1; i >= 0; i--)
            if(theme.handleInput(components.get(i), mouseX, mouseY, mouseButton, mouseEventType))
                break;
    }

    /**
     * Calls the {@link Theme}'s keyTyped function for every {@link Component} in
     * the stage.
     *
     * @param typedChar the character which was typed.
     * @param keyCode the keycode of the key which was pressed.
     *
     * @see GuiScreen
     */
    public void keyTyped(char typedChar, int keyCode)
    {
        if(keyCode == Keyboard.KEY_ESCAPE)
        {
            Irrlicht.getMinecraft()
                    .displayGuiScreen(null);
            return;
        }

        for(Component component : stage.getComponents())
            if(theme.handleKeyTyped(component, typedChar, keyCode))
                break;
    }

    /**
     * Called every tick used to animate things 1 tick = 1 20th of a second.
     *
     * @see GuiScreen
     */
    public void update()
    {
        theme.update(stage);
    }

    /**
     * Called when focusing a {@link Component}.
     *
     * @param component the {@link Component} which is supposed to be focused.
     */
    public void focus(Component component)
    {
        if(component instanceof Focusable)
        {
            final Focusable focusable = (Focusable) component;
            if(this.focused != null)
                ((Focusable)this.focused).loseFocus();
            this.focused = component;
            focusable.gainFocus();
            if(stage.getComponents().contains(component))
            {
                stage.remove(component);
                stage.add(component);
            }
        }
    }

    /**
     * @param component the {@link Component} for which should be determined if it is
     *                  focused or not.
     *
     * @return whether or not the {@link Component} is currently focused.
     */
    public boolean isFocused(Component component)
    {
        return component == focused;
    }

    /**
     * Called when the window gets resized or the {@link GuiScreen} is set.
     *
     * @see GuiScreen
     * @see Minecraft#displayGuiScreen(GuiScreen)
     */
    public void init()
    {
        focused = null;
        if(theme == null)
            loadTheme(Theme.createVisTheme(this));
        else
            try
            {
                theme.init();
            }
            catch (ParserException e)
            {
                e.printStackTrace();
            }
        stage.layoutComponents();
    }

    /**
     * Called before the {@link GuiScreen} is destroyed.
     *
     * @see GuiScreen
     * @see Minecraft#displayGuiScreen(GuiScreen)
     */
    public void leave()
    {
        focused = null;
        theme.free();
    }

    /**
     * Properly loads the given {@link Theme} and initializes it.
     *
     * @param theme the {@link Theme} which will be loaded and initialized.
     */
    public void loadTheme(Theme theme)
    {
        if(this.theme != null)
            this.theme.free();
        try
        {
            theme.init();
            this.theme = theme;
        }
        catch (ParserException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Used when adding a {@link com.nur1popcorn.irrlicht.gui.themes.parsing.Style} to an already
     * loaded {@link Theme}.
     */
    public void reloadTheme()
    {
        if(theme != null)
        {
            theme.free();
            try
            {
                theme.init();
            }
            catch (ParserException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the stage used to store all the {@link Component}s.
     */
    public Container getStage()
    {
        return stage;
    }

    /**
     * @return the {@link Theme} used to handle all the {@link Component}s.
     */
    public Theme getTheme()
    {
        return theme;
    }
}
