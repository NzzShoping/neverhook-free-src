package ru.neverhook.feature.combat;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.EntityLivingBase;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.ui.notification.NotificationManager;
import ru.neverhook.ui.notification.NotificationType;
import ru.neverhook.utils.combat.RotationUtil;
import ru.neverhook.utils.movement.MovementUtil;

public class TargetStrafe extends Feature {

    public static Setting range;
    public static Setting spd;
    public int direction = -1;

    public TargetStrafe() {
        super("TargetStrafe", Category.Combat);
        Main.instance.setmgr.addSetting(spd = new Setting("Strafe Speed", this, 0.23D, 0.1D, 2, 0.01));
        Main.instance.setmgr.addSetting(range = new Setting("Distance", this, 3, 0.1, 6.0, 0.1));
        Main.instance.setmgr.addSetting(new Setting("Wellmore Exploit", this, true));
        Main.instance.setmgr.addSetting(new Setting("AutoJump", this, true));
        Main.instance.setmgr.addSetting(new Setting("AutoShift", this, false));
    }

    public void onMotionUpdate() {
        if (KillAura.target == null)
            return;
        EntityLivingBase entity = KillAura.target;
        float[] rotation = RotationUtil.getRotations(entity, false);
        if (mc.player.getDistanceToEntity(entity) <= range.getValDouble()) {
            MovementUtil.setMotion(spd.getValFloat(), rotation[0], this.direction, 0);
        } else {
            MovementUtil.setMotion(spd.getValFloat(), rotation[0], this.direction, 1);
        }
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        this.setSuffix("" + range.getValFloat());
        if (KillAura.target == null)
            return;
        if (mc.player.isCollidedHorizontally)
            switchDirection();
        if (mc.gameSettings.keyBindLeft.isPressed())
            this.direction = 1;
        if (mc.gameSettings.keyBindRight.isPressed())
            this.direction = -1;
        if (mc.player.getDistanceToEntity(KillAura.target) <= KillAura.range.getValFloat()) {
            if (KillAura.target.getHealth() > 0) {
                if (Main.instance.setmgr.getSettingByName("AutoJump").getValue() && Main.instance.featureManager.getFeatureByClass(KillAura.class).getState() && Main.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState()) {
                    if (mc.player.onGround) {
                        mc.player.jump();
                        if (Main.instance.setmgr.getSettingByName("Wellmore Exploit").getValue()) {
                            mc.player.motionY = 0.419973;
                        }
                    }
                    if (Main.instance.setmgr.getSettingByName("Wellmore Exploit").getValue()) {
                        if (mc.player.motionY > 0 && !mc.player.onGround) {
                            mc.player.motionY -= 0.00205;
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    public void onSwitchDir(EventUpdate event) {

        if (mc.currentScreen instanceof GuiGameOver) {
            toggle();
            NotificationManager.queue(getName(), "was Toggled Off", 2, NotificationType.INFO);
            return;
        }
        if (mc.player.ticksExisted <= 1) {
            toggle();
            NotificationManager.queue(getName(), "was Toggled Off", 2, NotificationType.INFO);
            return;
        }

        if (mc.player.getDistanceToEntity(KillAura.target) <= KillAura.range.getValFloat()) {
            if (KillAura.target == null) return;
            if (mc.player.isCollidedHorizontally) {
                switchDirection();
            }

            if (Main.instance.setmgr.getSettingByName("AutoShift").getValue()) {
                if (KillAura.target == null)
                    return;
                mc.gameSettings.keyBindSneak.pressed = getState() && KillAura.target != null && mc.player.fallDistance > Main.instance.setmgr.getSettingByName("Crits Fall Distance").getValFloat() + 0.1;
            }

            if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                direction = 1;
            }

            if (mc.gameSettings.keyBindRight.isKeyDown()) {
                direction = -1;
            }
        }
    }

    private void switchDirection() {
        if (direction == 1) {
            direction = -1;
        } else {
            direction = 1;
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        if (mc.player.getDistanceToEntity(KillAura.target) <= KillAura.range.getValFloat()) {
            if (KillAura.target == null)
                return;
            if (Main.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target != null) {
                if (mc.player.isCollidedHorizontally) {
                    this.switchDirection();
                }
                if (KillAura.target.getHealth() > 0) {
                    onMotionUpdate();
                }
            }
        }
    }
}