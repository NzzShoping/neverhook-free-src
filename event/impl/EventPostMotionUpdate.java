package ru.neverhook.event.impl;

import ru.neverhook.event.callables.EventCancellable;

public class EventPostMotionUpdate extends EventCancellable {

    public float pitch;
    private float yaw;

    public EventPostMotionUpdate(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
