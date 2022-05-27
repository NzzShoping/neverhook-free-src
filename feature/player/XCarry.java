package ru.neverhook.feature.player;

import net.minecraft.network.play.client.CPacketCloseWindow;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventSendPacket;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class XCarry extends Feature {

    public XCarry() {
        super("XCarry", Category.Player);
    }

    @EventTarget
    public void onPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketCloseWindow) {
            CPacketCloseWindow cPacketCloseWindow = (CPacketCloseWindow) event.getPacket();
            event.setCancelled(true);
        }
    }
}
