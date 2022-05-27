package ru.neverhook.event.impl;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import ru.neverhook.event.callables.EventCancellable;

public class EventSendPacket extends EventCancellable {
    private final boolean sending;
    private Packet packet;

    public EventSendPacket(Packet packet, boolean sending) {
        this.packet = packet;
        this.sending = sending;
    }

    public boolean isSending() {
        return this.sending;
    }

    public boolean isRecieving() {
        return !this.sending;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet<? extends INetHandler> packet) {
        this.packet = packet;
    }
}
