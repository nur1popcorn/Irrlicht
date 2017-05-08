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

package com.nur1popcorn.irrlicht.gui.themes.parsing;

import com.nur1popcorn.irrlicht.gui.themes.parsing.exceptions.ParserException;
import com.nur1popcorn.irrlicht.gui.themes.parsing.values.Value;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The {@link Style} are used by are used by {@link com.nur1popcorn.irrlicht.gui.themes.Theme}s
 * to easily modify {@link com.nur1popcorn.irrlicht.gui.themes.ComponentHandler}s.
 *
 * @see com.nur1popcorn.irrlicht.gui.themes.Theme
 * @see com.nur1popcorn.irrlicht.gui.themes.ComponentHandler
 * @see com.nur1popcorn.irrlicht.gui.GuiManager
 * @see Tokenizer
 * @see Token
 * @see TokenType
 * @see TokenInfo
 * @see Value
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Style
{
    private final String name;
    private Map<String, Value> valueMap = new HashMap<>();

    //prevent construction :/
    private Style(String name)
    {
        this.name = name;
    }

    /**
     * @param source the source to be turned into a {@link Style}.
     *
     * @throws ParserException if a invalid {@link Token} or a malformed {@link Token}
     *                         is passed to the function.
     *
     * @see Tokenizer#tokenize()
     * @see #parse(LinkedList)
     *
     * @return a parsed version of the source.
     */
    public static List<Style> parse(String source) throws ParserException
    {
        final Tokenizer tokenizer = Tokenizer.createStyleTokenizer(source);
        tokenizer.tokenize();
        tokenizer.setSource(null);
        return parse(tokenizer.getTokens());
    }

    /**
     * @param b the byte array to be converted into an integer.
     *
     * @return an integer for the byte array.
     */
    private static int byteArrayToInt(byte[] b)
    {
        return b[3] & 0xFF |
              (b[2] & 0xFF) << 8 |
              (b[1] & 0xFF) << 16 |
              (b[0] & 0xFF) << 24;
    }

    /**
     * @param tokens a pre tokenized version of a {@link Style}.
     *
     * @throws ParserException if a invalid version is passed to the function.
     *
     * @see Tokenizer#compile()
     * @see #parse(LinkedList)
     *
     * @return a parsed version of the {@link Token}s provided.
     */
    public static List<Style> parse(byte[] tokens) throws ParserException
    {
        final int version = byteArrayToInt(Arrays.copyOf(tokens, 4));
        if(IntStream.of(Tokenizer.COMPATIBLE_VERSIONS).anyMatch(compatible -> compatible == version))
        {
            LinkedList<Token> finalTokens = new LinkedList<>();
            Stream.of(new String(Arrays.copyOfRange(tokens, 4, tokens.length)).split("\n")).forEach(s -> finalTokens.add(new Token(byteArrayToInt(Arrays.copyOf(s.getBytes(), 4)), new String(Arrays.copyOfRange(s.getBytes(), 4, s.getBytes().length)))));
            return parse(finalTokens);
        }
        throw new ParserException("This version is not compatible with the style you are trying to use. Consider updating: " + Integer.toHexString(version));
    }

    /**
     * @param tokens a list of {@link Token}s.
     *
     * @throws ParserException if a invalid {@link Token} or a malformed {@link Token}
     *                         is passed to the function.
     *
     * @return a parsed version of the {@link Token}s provided.
     */
    public static List<Style> parse(LinkedList<Token> tokens) throws ParserException
    {
        final List<Style> styles = new ArrayList<>();
        Style currentStyles[] = null;
        for(Token token : tokens)
            switch(TokenType.values()[token.getTokenType()])
            {
                case CONSTANT:
                {
                    if(currentStyles != null)
                        throw new ParserException("Failed to parse style you cant declare constants inside of a class: " + token.getSource(), token);
                    if(!token.getSource().startsWith("define(") || !token.getSource().endsWith(")"))
                        throw new ParserException("Failed to parse style you cant invalid declaration of a constant: " + token.getSource(), token);
                    String values[] = token.getSource().replace("define(", "").replace(")", "").split(",", 2);
                    if(values.length != 2)
                        throw new ParserException("Failed to parse style due to a constant being declared incorrectly: " + token.getSource(), token);
                    for(Token otherToken : tokens)
                        otherToken.setSource(otherToken.getSource().replace(values[0], values[1]));
                }
                break;
                case START_CLASS:
                {
                    if(currentStyles != null)
                        throw new ParserException("Failed to parse style you cant start a new class without closing the old one: ", token);
                    String names[] = token.getSource().replaceAll("[.{]+?", "").split(",");
                    currentStyles = new Style[names.length];
                    for(int i = 0; i < names.length; i++)
                        currentStyles[i] = new Style(names[i]);
                }
                break;
                case VALUE:
                {
                    if(currentStyles == null)
                        throw new ParserException("Failed to parse style you cant declare a value outside of a class: " + token.getSource(), token);
                    String values[] = token.getSource().replaceFirst("-", "").replaceFirst("(?s);(?!.*?;)", "").split(":", 2);
                    if(values.length != 2)
                        throw new ParserException("Failed to parse style a value needs a key and a value: " + token.getSource(), token);
                    for(Style style : currentStyles)
                        style.putValue(values[0], Value.parse(values[1]));
                }
                break;
                case END_CLASS:
                {
                    if(currentStyles == null)
                        throw new ParserException("Failed to parse style you cant end a class without starting one: " + token.getSource(), token);
                    final Iterator<Style> iterator = styles.iterator();
                    while(iterator.hasNext())
                    {
                        final Style style = iterator.next();
                        for(Style newStyle : currentStyles)
                            if(style.getName().equals(newStyle.getName()))
                            {
                                newStyle.merge(style);
                                iterator.remove();
                            }
                    }
                    styles.addAll(Arrays.asList(currentStyles));
                    currentStyles = null;
                }
                break;
                default:
                    throw new ParserException("Failed to parse style due to a unexpected token: " + token.getSource(), token);
            }
        return styles;
    }

    /**
     * @throws ParserException when a {@link Value} fails to be serialized.
     *
     * @see Value#serialize()
     *
     * @return a serialized version of the {@link Style}.
     */
    public String serialize() throws ParserException
    {
        StringBuilder stringBuilder = new StringBuilder()
                     .append(TokenType.START_CLASS.getTokenFormat(name))
                     .append("\n");
        for(String key : valueMap.keySet())
            stringBuilder.append("   ")
                         .append(TokenType.VALUE.getTokenFormat(key, valueMap.get(key).serialize()))
                         .append("\n");
        stringBuilder.append(TokenType.END_CLASS.getTokenFormat())
                     .append("\n");
        return stringBuilder.toString();
    }

    /**
     * Merges this {@link Style} with another {@link Style}.
     *
     * @param style the {@link Style} that this {@link Style} is supposed to be merged with.
     */
    public void merge(Style style) throws ParserException
    {
        for(String key : valueMap.keySet())
        {
            Value otherValue = style.getValue(key);
            if(otherValue != null && !valueMap.get(key).getClass().isAssignableFrom(otherValue.getClass()))
                throw new ParserException("Could not merge styles due to a value type not matching: " + key + ":" + otherValue.serialize());
        }
        valueMap.putAll(style.valueMap);
    }

    /**
     * Adds the key and associated {@link Value} to the {@link Style}.
     *
     * @param key the key used to identify the {@link Value}.
     * @param value the {@link Value} mapped to the key.
     */
    public void putValue(String key, Value value)
    {
        valueMap.put(key, value);
    }

    /**
     * @param key the used to identify the {@link Value}.
     *
     * @return a {@link Value} mapped to the key provided.
     */
    public Value getValue(String key)
    {
        return valueMap.get(key);
    }

    /**
     * @return the name of the {@link Style}.
     */
    public String getName()
    {
        return name;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj == this || (obj instanceof Style && ((Style) obj).getName().equals(getName()));
    }
}
