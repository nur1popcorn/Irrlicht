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

import com.nur1popcorn.irrlicht.gui.font.FontRenderer;

/**
 * The {@link Label} is a text {@link Component}.
 *
 * @see Component
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Label extends Component
{
    public static FontRenderer fontRenderer;
    private String text,
                   link;

    public Label(String text)
    {
        setText(text);
    }

    public Label(String text, String link)
    {
        this(text);
        this.link = link;
    }

    /**
     * Sets the labels text.
     *
     * @param text the labels text.
     */
    public void setText(String text)
    {
        if(fontRenderer != null)
        {
            setPrefWidth(fontRenderer.getStringWidth(text));
            setPrefHeight(fontRenderer.getStringHeight(text));
        }
        this.text = text;
    }

    /**
     * @return the labels text.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Sets the labels link.
     *
     * @param link the labels link.
     */
    public void setLink(String link)
    {
        this.link = link;
    }

    /**
     * @return the labels link.
     */
    public String getLink()
    {
        return link;
    }

    /**
     * @return whether or not the label has a link.
     */
    public boolean isLink()
    {
        return link != null;
    }
}
