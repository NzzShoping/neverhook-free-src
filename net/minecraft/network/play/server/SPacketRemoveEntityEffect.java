package net.minecraft.network.play.server;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.io.IOException;

public class SPacketRemoveEntityEffect implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private Potion effectId;

    public SPacketRemoveEntityEffect() {
    }

    public SPacketRemoveEntityEffect(int entityIdIn, Potion potionIn) {
        this.entityId = entityIdIn;
        this.effectId = potionIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.effectId = Potion.getPotionById(buf.readUnsignedByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(Potion.getIdFromPotion(this.effectId));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleRemoveEntityEffect(this);
    }

    @Nullable
    public Entity getEntity(World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }

    @Nullable
    public Potion getPotion() {
        return this.effectId;
    }
}
