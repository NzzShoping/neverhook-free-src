package ru.neverhook.event.impl;

import net.minecraft.util.EnumHandSide;
import ru.neverhook.event.Event;

public class EventTransformSideFirstPerson implements Event {

    private final EnumHandSide enumHandSide;

    public EventTransformSideFirstPerson(EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }

}
