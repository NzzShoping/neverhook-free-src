package ru.neverhook.event.impl;

import net.minecraft.client.gui.ScaledResolution;
import ru.neverhook.event.Event;

public class EventRenderGui implements Event {

    private final ScaledResolution scaledResolution;
    private final float partialTicks;

    public EventRenderGui(ScaledResolution scaledResolution, float partialTicks) {
        this.scaledResolution = scaledResolution;
        this.partialTicks = partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

}
