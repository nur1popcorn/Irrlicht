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

package com.nur1popcorn.irrlicht.modules;

import com.nur1popcorn.irrlicht.modules.impl.combat.AutoClicker;
import com.nur1popcorn.irrlicht.modules.impl.combat.Velocity;
import com.nur1popcorn.irrlicht.modules.impl.misc.Timer;
import com.nur1popcorn.irrlicht.modules.impl.movement.Fly;
import com.nur1popcorn.irrlicht.modules.impl.movement.NoFall;
import com.nur1popcorn.irrlicht.modules.impl.movement.Speed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@link ModuleManager} stores and handles all available {@link Module}s.
 *
 * @see Module
 * @see Category
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class ModuleManager
{
    private List<Module> modules = new ArrayList<>();

    //prevent construction :/
    private ModuleManager()
    {}

    /**
     * A Factory method used to create a default {@link ModuleManager}.
     *
     * @see Module
     *
     * @return a default {@link ModuleManager}.
     */
    public static ModuleManager createModuleManager()
    {
        final ModuleManager moduleManager = new ModuleManager();
        //combat
        moduleManager.register(new AutoClicker());
        moduleManager.register(new Velocity());
        //misc
        moduleManager.register(new Timer());
        //movement
        moduleManager.register(new Fly());
        moduleManager.register(new NoFall());
        moduleManager.register(new Speed());
        moduleManager.init();
        return moduleManager;
    }

    /**
     * Adds a {@link Module}.
     *
     * @param module the {@link Module} to be added.
     */
    public void register(Module module)
    {
        modules.add(module);
    }

    /**
     * Removes a {@link Module}.
     *
     * @param module the {@link Module} to be removed.
     */
    public void unregister(Module module)
    {
        modules.remove(module);
    }

    /**
     * Initialises all of the {@link Module}s.
     */
    public void init()
    {
        modules.forEach(Module::init);
    }

    /**
     * @param clazz the class for which the {@link Module} should be returned.
     *
     * @return a {@link Module} based on the class provided.
     */
    public Module getModule(Class clazz)
    {
        for(Module module : modules)
            if(module.getClass() == clazz)
                return module;
        return null;
    }

    /**
     * @param name the name for which the {@link Module} should be returned.
     *
     * @see Module#getName()
     *
     * @return a {@link Module} based on the name provided.
     */
    public Module getModule(String name)
    {
        for(Module module : modules)
            if(module.getName().equals(name))
                return module;
        return null;
    }

    /**
     * @param category the {@link Category} from which all the modules should be returned.
     *
     * @see Module#getCategory()
     *
     * @return all the {@link Module}s in the category provided.
     */
    public List<Module> getModulesByCategory(Category category)
    {
        return modules.stream()
                .filter(module -> module.getCategory() == category)
                .collect(Collectors.toList());
    }
}
