package net.minecraft.network.play.client;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class CPacketConfirmTeleport implements Packet<INetHandlerPlayServer> {
    private int telportId;

    public CPacketConfirmTeleport() {
    }

    public CPacketConfirmTeleport(int teleportIdIn) {
        this.telportId = teleportIdIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.telportId = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.telportId);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processConfirmTeleport(this);
    }

    public int getTeleportId() {
        return this.telportId;
    }
}
