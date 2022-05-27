package ru.neverhook.event.impl;

import ru.neverhook.event.Event;

public class Event3D implements Event {
    private final float partialTicks;

    public Event3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
