package ru.neverhook.event.impl;

import net.minecraft.entity.Entity;
import ru.neverhook.event.callables.EventCancellable;

public class EventAttackPacket extends EventCancellable {

    private final Entity targetEntity;

    public EventAttackPacket(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Entity getTargetEntity() {
        return this.targetEntity;
    }

}
