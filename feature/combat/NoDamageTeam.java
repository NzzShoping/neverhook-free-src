package ru.neverhook.feature.combat;

import net.minecraft.network.play.client.CPacketUseEntity;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventSendPacket;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class NoDamageTeam extends Feature {

    public NoDamageTeam() {
        super("NoDamageTeam", Category.Combat);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK && Main.instance.friendManager.isFriend(mc.objectMouseOver.entityHit.getName())) {
                event.setCancelled(true);
            }
        }
    }
}
