package ru.neverhook.event.impl;

import net.minecraft.network.Packet;
import ru.neverhook.event.callables.EventCancellable;

public class EventReceivePacket
        extends EventCancellable {
    private Packet packet;

    public EventReceivePacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
