package ru.neverhook.feature.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventStep;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.other.TimerUtils;

import java.util.ArrayList;

public class Step extends Feature {

    public static TimerUtils lastStep = new TimerUtils();
    public static TimerUtils time = new TimerUtils();
    private final Setting delay;
    public Setting height;
    boolean resetTimer;

    public Step() {
        super("Step", Category.Movement);
        ArrayList<String> step = new ArrayList<>();
        step.add("Motion");
        Main.instance.setmgr.addSetting(new Setting("Step Mode", this, "Motion", step));
        Main.instance.setmgr.addSetting(this.height = new Setting("Height", this, 1.5, 1, 10, 0.1));
        Main.instance.setmgr.addSetting(this.delay = new Setting("Step Delay", this, 0.1, 0, 1, 0.1));
    }

    @EventTarget
    public void onStepConfirm(EventStep step) {
        String mode = Main.instance.setmgr.getSettingByName("Step Mode").getValString();
        this.setSuffix(mode);
        float delay1 = delay.getValFloat() * 1000;
        double stepValue = height.getValDouble();
        float timer = 0.37F;

        if (resetTimer) {
            resetTimer = false;
            mc.timer.timerSpeed = 1;
        }
        if (!mc.player.isInLiquid()) {
            if (step.isPre()) {
                if (mc.player.isCollidedVertically && !mc.gameSettings.keyBindJump.isPressed() && time.hasReached(delay1)) {
                    step.setStepHeight(stepValue);
                    step.setActive(true);
                }
            } else {
                double rheight = mc.player.getEntityBoundingBox().minY - mc.player.posY;
                boolean canStep = rheight >= 0.625;
                if (canStep) {
                    lastStep.reset();
                    time.reset();
                }
                if (mode.equalsIgnoreCase("Motion")) {
                    if (canStep) {
                        mc.timer.timerSpeed = timer - (rheight >= 1 ? Math.abs(1 - (float) rheight) * (timer * 0.7f) : 0);
                        if (mc.timer.timerSpeed <= 0.05f) {
                            mc.timer.timerSpeed = 0.05f;
                        }
                        resetTimer = true;
                        matrixStep();
                    }
                }
            }
        }
    }

    void matrixStep() {
        double posX = mc.player.posX;
        double posZ = mc.player.posZ;
        double y = mc.player.posY;
        double first = 0.42;
        mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + first, posZ, false));
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.625f;
        mc.timer.timerSpeed = 1f;
        super.onDisable();
    }
}