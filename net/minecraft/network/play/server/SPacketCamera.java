package net.minecraft.network.play.server;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.io.IOException;

public class SPacketCamera implements Packet<INetHandlerPlayClient> {
    public int entityId;

    public SPacketCamera() {
    }

    public SPacketCamera(Entity entityIn) {
        this.entityId = entityIn.getEntityId();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleCamera(this);
    }

    @Nullable
    public Entity getEntity(World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }
}
