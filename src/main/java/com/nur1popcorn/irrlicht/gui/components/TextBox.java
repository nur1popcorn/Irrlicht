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

/**
 * The {@link TextBox} is a rectangular {@link Component} in which one can type text. It can
 * also be used to enter passwords and match the given string.
 *
 * @see LockableComponent
 * @see Focusable
 * @see Observer
 * @see Observable
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class TextBox extends LockableComponent implements Focusable
{
    private Observable<String> text = new Observable<>("");
    private String regex;
    private boolean password,
                    verifyInput;
    private int maxCharacterSize,
                cursorPosition,
                selectionPosition,
                scrollOffset;

    /**
     * @return the text of the {@link TextBox}.
     */
    public String getText()
    {
        return text.get();
    }

    /**
     * Sets the text of the {@link TextBox}.
     *
     * @param text the text which should be displayed in the {@link TextBox}.
     */
    public void setText(String text)
    {
        this.text.set(text);
    }

    /**
     * Adds a {@link Observer} to the {@link TextBox}'s value.
     *
     * @param observer the {@link Observer} used to observe the value.
     */
    public void onChange(Observer<String> observer)
    {
        text.register(observer);
    }

    /**
     * Sets whether or not the {@link TextBox} should obfuscate it's text.
     *
     * @param password whether or not the {@link TextBox} should obfuscate it's text.
     */
    public void setPassword(boolean password)
    {
        this.password = password;
    }

    /**
     * @return whether or not the {@link TextBox} is a password field.
     */
    public boolean isPassword()
    {
        return password;
    }

    /**
     * Sets whether or not the input should be verified before set.
     *
     * @param verifyInput whether or not the input should be verified before set.
     */
    public void setVerifyInput(boolean verifyInput)
    {
        this.verifyInput = verifyInput;
    }

    /**
     * @return whether or not the input should be verified before set.
     */
    public boolean shouldVerifyInput()
    {
        return verifyInput;
    }

    /**
     * @return the maximum amount of characters allowed in the {@link TextBox}.
     */
    public int getMaxCharacterSize()
    {
        return maxCharacterSize;
    }

    /**
     * Sets the maximum amount of characters allowed in the {@link TextBox}.
     *
     * @param maxCharacterSize the maximum amount of characters allowed in the {@link TextBox}.
     */
    public void setMaxCharacterSize(int maxCharacterSize)
    {
        this.maxCharacterSize = maxCharacterSize;
    }

    /**
     * Sets the regex used to verify the input.
     *
     * @param regex the regex used to verify the input.
     */
    public void setRegex(String regex)
    {
        this.regex = regex;
    }

    /**
     * @return the regex used to verify the input.
     */
    public String getRegex()
    {
        return regex;
    }

    /**
     * @param text the input provided for which should be check whether or not it is valid.
     *
     * @return whether or not the input provided would be valid.
     */
    public boolean isValid(String text)
    {
        return regex != null && text.matches(regex);
    }

    /**
     * @return whether or not the current input is valid.
     */
    public boolean hasError()
    {
        return regex != null && !text.get().matches(regex);
    }

    /**
     * Sets the cursors position.
     *
     * @param cursorPosition the cursors position.
     */
    public void setCursorPosition(int cursorPosition)
    {
        this.cursorPosition = cursorPosition;
    }

    /**
     * @return the cursors position.
     */
    public int getCursorPosition()
    {
        return cursorPosition;
    }

    /**
     * Sets the selection position.
     *
     * @param selectionPosition the selection position.
     */
    public void setSelectionPosition(int selectionPosition)
    {
        this.selectionPosition = selectionPosition;
    }

    /**
     * @return the selection position.
     */
    public int getSelectionPosition()
    {
        return selectionPosition;
    }

    /**
     * Sets the scroll offset which is used to scroll vertically.
     *
     * @param scrollOffset the scroll offset which is used to scroll vertically.
     */
    public void setScrollOffset(int scrollOffset)
    {
        this.scrollOffset = scrollOffset;
    }

    /**
     * @return the scroll offset which is used to scroll vertically.
     */
    public int getScrollOffset()
    {
        return scrollOffset;
    }
}
