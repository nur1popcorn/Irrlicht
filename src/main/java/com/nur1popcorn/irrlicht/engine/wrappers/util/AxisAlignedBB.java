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

package com.nur1popcorn.irrlicht.engine.wrappers.util;

import com.nur1popcorn.irrlicht.engine.mapper.DiscoveryMethod;
import com.nur1popcorn.irrlicht.engine.mapper.Mapper;
import com.nur1popcorn.irrlicht.engine.wrappers.Wrapper;
import com.nur1popcorn.irrlicht.engine.wrappers.entity.Entity;

/**
 * The {@link AxisAlignedBB} class represents a entity's bounding box.
 *
 * @see Wrapper
 * @see Entity
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
@DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIRST_MATCH,
                 declaring = Entity.class)
public interface AxisAlignedBB extends Wrapper
{
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.CONSTRUCTOR)
    public void construct(double x, double y, double z, double xOther, double yOther, double zOther);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public double getMinX();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getMinY();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getMinZ();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getMaxX();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public double getMaxY();
    
    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public double getMaxZ();

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_START)
    public void setMinX(double minX);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setMinY(double minY);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setMinZ(double minZ);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setMaxX(double maxX);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD)
    public void setMaxY(double maxY);

    @DiscoveryMethod(checks = Mapper.DEFAULT | Mapper.FIELD | Mapper.STRUCTURE_END)
    public void setMaxZ(double maxZ);
}
