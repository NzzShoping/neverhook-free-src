package ru.neverhook.feature.other;

import net.minecraft.client.gui.GuiGameOver;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.utils.other.ChatUtils;

public class DeathCoords extends Feature {

    public DeathCoords() {
        super("DeathCoords", Category.World);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getHealth() <= 0 && mc.currentScreen instanceof GuiGameOver) {
            int x = mc.player.getPosition().getX();
            int y = mc.player.getPosition().getY();
            int z = mc.player.getPosition().getZ();
            if (mc.player.ticksExisted % 8 == 0) {
                ChatUtils.addChatMessage("Death Coords: " + "X: " + x + " Y: " + y + " Z: " + z);
            }
        }
    }
}
