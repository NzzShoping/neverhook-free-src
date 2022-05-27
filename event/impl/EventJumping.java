package ru.neverhook.event.impl;

import ru.neverhook.event.Event;
import ru.neverhook.event.callables.EventCancellable;

public class EventJumping extends EventCancellable implements Event {

    private float yaw;

    public EventJumping(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
