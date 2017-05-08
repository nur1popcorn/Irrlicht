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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@link Tokenizer} is used to tokenize the source.
 *
 * @see Token
 * @see TokenInfo
 * @see Style
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Tokenizer
{
    public static final int STYLE_VERSION = 0x0;

    public static final int[] COMPATIBLE_VERSIONS = new int[]
    {
        0x0
    };

    private LinkedList<Token> tokens = new LinkedList<>();
    private List<TokenInfo> tokenInfos = new ArrayList<>();
    private String source;

    //prevent construction :/
    private Tokenizer(String source)
    {
        this.source = source;
    }

    /**
     * <p>
     *    Factory method to create a {@link Tokenizer} that tokenizes {@link Style}s.
     * </p>
     * <p>
     *    basic style layout:
     * </p>
     * <pre>
     * define(PI, 3.14159265359)
     * .example, otherExample {
     *    -key: 'value';
     *    -otherKey: PI;
     *    -color: #ff00ff;
     * }
     * </pre>
     *
     * @param source the source to be tokenized.
     *
     * @return a {@link Tokenizer} used to tokenize {@link Style}s.
     */
    public static Tokenizer createStyleTokenizer(String source)
    {
        final Tokenizer tokenizer = new Tokenizer(source);
        tokenizer.add(new TokenInfo(Pattern.compile("define\\(([a-zA-Z_-]+?),([a-zA-Z0-9.+-_]+?)\\)"), TokenType.CONSTANT.ordinal()),
                      new TokenInfo(Pattern.compile("\\.([a-zA-Z_,-]+?)\\{"), TokenType.START_CLASS.ordinal()),
                      new TokenInfo(Pattern.compile("-([a-zA-Z_-]+?):?(')(([^']*)';)|[a-zA-Z0-9.+-_#]+?;"), TokenType.VALUE.ordinal()),
                      new TokenInfo(Pattern.compile("\\}"), TokenType.END_CLASS.ordinal()));
        return tokenizer;
    }

    /**
     * Adds a {@link Token} to be recognized by the {@link Tokenizer}.
     *
     * @param tokenInfo information about the {@link Token}s which are recognized.
     */
    public void add(TokenInfo... tokenInfo)
    {
        tokenInfos.addAll(Arrays.asList(tokenInfo));
    }

    /**
     * Turns the source into {@link Token}s.
     *
     * @see #getTokens()
     */
    public void tokenize() throws ParserException
    {
        String source = this.source.replaceAll("\\s","");
        tokens.clear();
        outer: while(!source.replace("\n", "").equals(""))
        {
            for(TokenInfo tokenInfo : tokenInfos)
            {
                final Matcher matcher = tokenInfo.getRegex().matcher(source);
                if(matcher.find() && source.replace("\n", "").startsWith(matcher.group()))
                {
                    tokens.add(new Token(tokenInfo.getType(), matcher.group()));
                    source = matcher.replaceFirst("\n");
                    continue outer;
                }
            }
            throw new ParserException("Could not tokenize source: " + this.source.replaceAll("\\s", ""));
        }
    }

    /**
     * Sets the source to be tokenized.
     *
     * @param source source to be tokenized.
     */
    public void setSource(String source)
    {
        this.source = source;
    }

    /**
     * @return The source to be tokenized.
     */
    public String getSource()
    {
        return source;
    }

    /**
     * @param i the integer to be converted.
     *
     * @return a integer converted into a byte array.
     */
    private static byte[] intToByteArray(int i)
    {
        return new byte[]
        {
            (byte) ((i >> 24) & 0xFF),
            (byte) ((i >> 16) & 0xFF),
            (byte) ((i >> 8) & 0xFF),
            (byte) (i & 0xFF)
        };
    }

    /**
     * @return the {@link Token}s as byte array to be read by a parser.
     */
    public byte[] compile() throws IOException
    {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(intToByteArray(STYLE_VERSION));
        for(Token token : tokens)
        {
            byteArrayOutputStream.write(intToByteArray(token.getTokenType()));
            byteArrayOutputStream.write((token.getSource() + "\n").getBytes());
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * @see #tokenize()
     *
     * @return the tokeized source.
     */
    public LinkedList<Token> getTokens()
    {
        return tokens;
    }

}
