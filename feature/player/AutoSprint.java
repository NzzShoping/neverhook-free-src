package ru.neverhook.feature.player;

import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class AutoSprint extends Feature {
    public AutoSprint() {
        super("AutoSprint", Category.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.player.setSprinting(mc.player.isMoving());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.setSprinting(mc.player.moveForward > 0 && !mc.player.isCollidedHorizontally || mc.player.isSprinting());
    }
}
