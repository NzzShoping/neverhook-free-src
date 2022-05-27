package ru.neverhook.feature.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class OldPositionTeleport extends Feature {

    public OldPositionTeleport() {
        super("OldPositionTeleport", Category.Movement);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotionUpdate event) {
        if (!this.getState())
            return;
        event.setGround(false);
    }

    @Override
    public void onDisable() {
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.5, mc.player.posZ, false));
        super.onDisable();
    }
}
