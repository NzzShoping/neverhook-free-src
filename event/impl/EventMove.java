package ru.neverhook.event.impl;

import ru.neverhook.event.Event;

public class EventMove implements Event {
    public double x;
    public double y;
    public double z;
    public float strafe;
    public float forward;
    public float yaw;
    public boolean canceled;

    public EventMove(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}