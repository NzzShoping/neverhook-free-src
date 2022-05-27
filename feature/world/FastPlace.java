package ru.neverhook.feature.world;

import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class FastPlace extends Feature {

    public FastPlace() {
        super("FastPlace", Category.World);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.rightClickDelayTimer = 0;
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 4;
        super.onDisable();
    }
}
