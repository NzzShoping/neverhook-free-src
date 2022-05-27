package ru.neverhook.event.impl;

import ru.neverhook.event.callables.EventCancellable;

public class EventMouse extends EventCancellable {

    public int key;

    public EventMouse(int key) {
        this.key = key;
    }
}
