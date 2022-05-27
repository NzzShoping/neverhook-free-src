package ru.neverhook.feature.world;

import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class NoBreakDelay extends Feature {
    public NoBreakDelay() {
        super("NoBreakDelay", Category.World);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.playerController.blockHitDelay = 0;
    }

}
