package ru.neverhook.feature.movement;

import net.minecraft.network.play.server.SPacketUpdateHealth;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.event.impl.EventReceivePacket;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.movement.MovementUtil;

import java.util.ArrayList;

public class Speed extends Feature {

    public static int stage;
    private final Setting strafing;
    public double moveSpeed;

    public Speed() {
        super("Speed", Category.Movement);
        ArrayList<String> speed = new ArrayList<>();
        speed.add("Sunrise");
        speed.add("Wellmore");
        Main.instance.setmgr.addSetting(new Setting("Speed Mode", this, "Wellmore", speed));
        Main.instance.setmgr.addSetting(this.strafing = new Setting("Strafing", this, false));
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketUpdateHealth) {
            String mode = Main.instance.setmgr.getSettingByName("Speed Mode").getValString();
            if (mode.equalsIgnoreCase("Sunrise")) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        if (this.getState()) {
            String mode = Main.instance.setmgr.getSettingByName("Speed Mode").getValString();
            this.setSuffix(mode);
            if (mode.equalsIgnoreCase("Sunrise")) {
                if (mc.player.onGround) {
                    mc.player.jump();
                }
                if (strafing.getValue()) {
                    MovementUtil.strafe();
                }
                if (mc.player.fallDistance > 0.2 && mc.player.ticksExisted % 60 > 39) {
                    mc.timer.timerSpeed = mc.player.ticksExisted % 2 == 0 ? 5000 : 1f;
                }
                if (mc.player.fallDistance > 1 || mc.player.fallDistance < 0.1) {
                    mc.timer.timerSpeed = 1;
                }
            } else if (mode.equalsIgnoreCase("Wellmore")) {
                if (strafing.getValue()) {
                    MovementUtil.strafe();
                }
                if (mc.player.onGround) {
                    mc.player.jump();
                    mc.player.speedInAir = 2f;
                    mc.player.jumpMovementFactor = 2f;
                } else {
                    mc.player.motionY = -20;
                    mc.timer.timerSpeed = 2.0f;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        mc.player.speedInAir = 0.02f;
        super.onDisable();
    }
}
