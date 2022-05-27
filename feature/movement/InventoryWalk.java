package ru.neverhook.feature.movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class InventoryWalk extends Feature {

    public InventoryWalk() {
        super("InventoryWalk", Category.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        KeyBinding[] keys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint};
        if (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditSign) return;
        byte b;
        int i;
        KeyBinding[] keyBindings;
        for (i = (keyBindings = keys).length, b = 0; b < i; ) {
            KeyBinding bind = keyBindings[b];
            bind.pressed = Keyboard.isKeyDown(bind.getKeyCode());
            b++;
        }
    }
}
