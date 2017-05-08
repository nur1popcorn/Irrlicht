package com.darkmagician6.eventapi.events;

import com.darkmagician6.eventapi.EventManager;

import java.lang.instrument.ClassDefinition;

/**
 * Irrlicht developed by nur1popcorn.
 */
public abstract class Event
{
    public final void call()
    {
        EventManager.call(this);
    }
}
