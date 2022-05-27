package ru.neverhook.event.callables;

import ru.neverhook.event.Event;
import ru.neverhook.event.Typed;

public abstract class EventTyped implements Event, Typed {

    private final byte type;

    protected EventTyped(byte eventType) {
        type = eventType;
    }

    @Override
    public byte getType() {
        return type;
    }

}
