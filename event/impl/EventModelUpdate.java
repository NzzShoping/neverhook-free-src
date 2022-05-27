package ru.neverhook.event.impl;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import ru.neverhook.event.callables.EventCancellable;

public class EventModelUpdate extends EventCancellable {
    private final EntityPlayer player;
    private final ModelPlayer model;

    public EventModelUpdate(EntityPlayer player, ModelPlayer model) {
        this.player = player;
        this.model = model;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public ModelPlayer getModel() {
        return this.model;
    }
}
