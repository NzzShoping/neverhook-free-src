package ru.neverhook.event.impl;

import ru.neverhook.event.Event;

public class EventKey implements Event {
    private final int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}
