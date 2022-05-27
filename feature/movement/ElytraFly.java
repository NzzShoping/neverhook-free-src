package ru.neverhook.feature.movement;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.movement.MovementUtil;

public class ElytraFly extends Feature {

    Setting motion;

    public ElytraFly() {
        super("ElytraFly", Category.Movement);
        Main.instance.setmgr.addSetting(this.motion = new Setting("Speed", this, 2, 0.5, 20, 0.1));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.isElytraFlying()) {
            mc.player.setVelocity(0.0, 0.0, 0.0);
            float speed = motion.getValFloat();
            if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.player.motionY = -speed;
            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.player.motionY = speed;
            if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()) {
                MovementUtil.setSpeed(speed);
            }
            if (mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
                MovementUtil.setSpeed(speed);
            }
        }
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(0.05f);
        if (!mc.player.capabilities.isCreativeMode) {
            mc.player.capabilities.allowFlying = false;
        }
        super.onDisable();
    }
}