package ru.neverhook.feature.movement;

import org.lwjgl.input.Keyboard;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.util.ArrayList;

public class NoSlowDown extends Feature {

    public static Setting percentage;

    public NoSlowDown() {
        super("NoSlowDown", Category.Movement);
        ArrayList<String> noSlow = new ArrayList<>();
        noSlow.add("Matrix");
        noSlow.add("Default");
        Main.instance.setmgr.addSetting(new Setting("NoSlowDown Mode", this, "Default", noSlow));
        Main.instance.setmgr.addSetting(percentage = new Setting("Percentage", this, 100, 0, 100, 1));
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        String mode = Main.instance.setmgr.getSettingByName("NoSlowDown Mode").getValString();
        this.setSuffix(percentage.getValDouble() + "% " + mode);
        if (mode.equalsIgnoreCase("Matrix")) {
            if (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                mc.gameSettings.keyBindSneak.pressed = mc.player.isUsingItem() && mc.player.isMoving() && mc.player.fallDistance > 0.1;
            }
        }
    }
}
