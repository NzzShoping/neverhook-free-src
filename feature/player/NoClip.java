package ru.neverhook.feature.player;

import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventInsideBlock;
import ru.neverhook.event.impl.EventPushOutBlock;
import ru.neverhook.event.impl.EventUpdateLiving;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class NoClip extends Feature {

    public NoClip() {
        super("NoClip", Category.Player);
    }

    @EventTarget
    public void insideBlock(EventInsideBlock event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onPushOfBlock(EventPushOutBlock event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onLivingUpdate(EventUpdateLiving event) {
        if (mc.player != null) {
            mc.player.noClip = true;
            mc.player.motionY = 0.0D;
            mc.player.onGround = false;
            mc.player.capabilities.isFlying = false;
            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.player.motionY += 0.5;
            if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.player.motionY -= 0.5;
            event.setCancelled(true);
        }
    }
}
