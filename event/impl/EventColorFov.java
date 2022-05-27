package ru.neverhook.event.impl;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import ru.neverhook.event.Event;

public class EventColorFov implements Event {

    private float red;
    private float green;
    private float blue;

    public EventColorFov(EntityRenderer renderer, Entity entity, IBlockState state, double renderPartialTicks, float red, float green, float blue) {
        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
    }

    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }
}