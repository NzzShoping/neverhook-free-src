package ru.neverhook.feature.player;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventReceivePacket;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class NoServerRotation extends Feature {

    public NoServerRotation() {
        super("NoServerRotation", Category.Player);
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
        }
    }
}
