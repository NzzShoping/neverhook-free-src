package ru.neverhook.feature.movement;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.movement.MovementUtil;

import java.util.ArrayList;

public class Fly extends Feature {

    private final Setting speed;

    public Fly() {
        super("Fly", Category.Movement);
        ArrayList<String> mode = new ArrayList<>();
        mode.add("Vanilla");
        mode.add("Wellmore");
        Main.instance.setmgr.addSetting(new Setting("Fly Mode", this, "Vanilla", mode));
        Main.instance.setmgr.addSetting(speed = new Setting("Fly Speed", this, 5f, 0.1, 15, 0.1));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.speedInAir = 0.02f;
        mc.timer.timerSpeed = 1.0f;
        mc.player.capabilities.isFlying = false;
        String mode = Main.instance.setmgr.getSettingByName("Fly Mode").getValString();
        if (mode.equalsIgnoreCase("Wellmore")) {
            mc.player.motionZ = 0;
            mc.player.motionX = 0;
        }
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("Fly Mode").getValString();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Vanilla")) {
            mc.player.capabilities.isFlying = true;
            MovementUtil.setSpeed(speed.getValFloat());
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY -= 0.1;
            } else if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY += 0.1;
            }
        } else if (mode.equalsIgnoreCase("Wellmore")) {
            if (mc.player.onGround) {
                mc.player.jump();
            } else {
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
                mc.player.motionY = -0.01;
                MovementUtil.setSpeed(speed.getValFloat());
                mc.player.speedInAir = 0.3f;
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.motionY -= 0.6;
                } else if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.motionY += 0.6;
                }
            }
        }
    }
}