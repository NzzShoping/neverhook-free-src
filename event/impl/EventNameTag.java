package ru.neverhook.event.impl;

import net.minecraft.entity.EntityLivingBase;
import ru.neverhook.event.callables.EventCancellable;

public class EventNameTag extends EventCancellable {
    private final EntityLivingBase entity;
    private String renderedName;

    public EventNameTag(EntityLivingBase entity, String renderedName) {
        this.entity = entity;
        this.renderedName = renderedName;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }

    public String getRenderedName() {
        return this.renderedName;
    }

    public void setRenderedName(String renderedName) {
        this.renderedName = renderedName;
    }
}