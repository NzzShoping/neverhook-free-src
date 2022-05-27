package ru.neverhook.feature.player;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventMove;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.movement.MovementUtil;

import java.util.ArrayList;

public class NoWeb extends Feature {

    public NoWeb() {
        super("NoWeb", Category.Player);
        ArrayList<String> web = new ArrayList<>();
        web.add("Matrix");
        Main.instance.setmgr.addSetting(new Setting("NoWeb Mode", this, "Matrix", web));
    }

    @EventTarget
    public void onMove(EventMove event) {
        String mode = Main.instance.setmgr.getSettingByName("NoWeb Mode").getValString();
        this.setSuffix(mode);
        if (getState()) {
            if (mode.equalsIgnoreCase("Matrix")) {
                if (mc.player.onGround && mc.player.isInWeb) {
                    mc.player.isInWeb = true;
                } else {
                    if (mc.gameSettings.keyBindJump.isKeyDown())
                        return;
                    mc.player.isInWeb = false;
                }
                if (mc.player.isInWeb && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    MovementUtil.setSpeed(event, 0.483);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        super.onDisable();
    }
}
