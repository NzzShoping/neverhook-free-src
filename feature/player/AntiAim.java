package ru.neverhook.feature.player;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.combat.GCDUtil;
import ru.neverhook.utils.other.MathUtils;

import java.util.ArrayList;

public class AntiAim extends Feature {

    public float rot = 0;

    public AntiAim() {
        super("AntiAim", Category.Player);
        ArrayList<String> antiAim = new ArrayList<>();
        antiAim.add("Spin");
        antiAim.add("OneTap");
        Main.instance.setmgr.addSetting(new Setting("AntiAim Mode", this, "Spin", antiAim));
        Main.instance.setmgr.addSetting(new Setting("Spin Speed", this, 1, 0, 10, 1));
        Main.instance.setmgr.addSetting(new Setting("Custom Pitch", this, true));
        Main.instance.setmgr.addSetting(new Setting("Custom Pitch Value", this, 90, -90, 90, 5));
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("AntiAim Mode").getValString();
        float spinSpeed = Main.instance.setmgr.getSettingByName("Spin Speed").getValFloat() * 10;
        if (mode.equalsIgnoreCase("Spin")) {
            float yaw = (float) Math.floor(spinAim(spinSpeed)) + MathUtils.getRandomInRange(1, -3);
            event.setYaw(GCDUtil.getFixedRotation(yaw));
        } else if (mode.equalsIgnoreCase("OneTap")) {
            float yaw = mc.player.rotationYaw + 180 + (mc.player.ticksExisted % 8 < 4 ? MathUtils.getRandomInRange(23, 33) : -MathUtils.getRandomInRange(23, 33));
            event.setYaw(GCDUtil.getFixedRotation(yaw));
        }
        if (Main.instance.setmgr.getSettingByName("Custom Pitch").getValue()) {
            float pitchValue = Main.instance.setmgr.getSettingByName("Custom Pitch Value").getValFloat();
            event.setPitch(pitchValue);
        }
    }

    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}
