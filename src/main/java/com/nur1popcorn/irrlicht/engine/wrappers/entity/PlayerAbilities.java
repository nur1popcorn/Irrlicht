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

package com.nur1popcorn.irrlicht.engine.wrappers.entity;

import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;

/**
 * The {@link PlayerAbilities} class is used to store a player's abilities.
 *
 * @see Wrapper
 * @see EntityPlayer
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.STRING_CONST,
                 declaring = EntityPlayer.class,
                 constants = { "invulnerable", "flying"})
public interface PlayerAbilities extends Wrapper
{
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public boolean isDamageDisabled();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public boolean isFlying();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public boolean isAllowedToFly();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public boolean isCreativeMode();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public boolean isAllowedToEdit();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public float getFlySpeed();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public float getWalkSpeed();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public void setDamageDisabled(boolean damageDisabled);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setFlying(boolean flying);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setAllowedToFly(boolean allowedToFly);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setCreativeMode(boolean creativeMode);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setAllowedToEdit(boolean allowedToEdit);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setFlySpeed(float flySpeed);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public void setWalkSpeed(float walkSpeed);
}
