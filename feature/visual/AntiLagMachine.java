package ru.neverhook.feature.visual;

import net.minecraft.world.EnumSkyBlock;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventWorldLight;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class AntiLagMachine extends Feature {

    public AntiLagMachine() {
        super("AntiLagMachine", Category.Visuals);
    }

    @EventTarget
    public void onWorldLight(EventWorldLight event) {
        if (getState()) {
            if (event.getEnumSkyBlock() == EnumSkyBlock.SKY) {
                event.setCancelled(true);
            }
        }
    }
}
