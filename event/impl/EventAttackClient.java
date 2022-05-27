package ru.neverhook.event.impl;

import net.minecraft.entity.Entity;
import ru.neverhook.event.callables.EventCancellable;

public class EventAttackClient extends EventCancellable {
    private final Entity entity;
    private final boolean preAttack;

    public EventAttackClient(Entity targetEntity, boolean preAttack) {
        this.entity = targetEntity;
        this.preAttack = preAttack;
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isPreAttack() {
        return preAttack;
    }

    public boolean isPostAttack() {
        return !preAttack;
    }
}
