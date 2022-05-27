package ru.neverhook.feature.player;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.combat.GCDUtil;
import ru.neverhook.utils.other.MathUtils;

public class AntiAFK extends Feature {

    public float rot = 0;

    public AntiAFK() {
        super("AntiAFK", Category.Player);
        Main.instance.setmgr.addSetting(new Setting("Send Message", this, true));
    }

    @EventTarget
    public void onPreUpdate(EventPreMotionUpdate event) {
        if (!mc.player.isMoving()) {
            float yaw = GCDUtil.getFixedRotation((float) (Math.floor(spinAim(1F)) + MathUtils.getRandomInRange(1, -4)));
            event.setYaw(yaw);
            mc.player.renderYawOffset = yaw;
            mc.player.rotationPitchHead = 0;
            mc.player.rotationYawHead = yaw;
            if (mc.player.ticksExisted % 500 == 0 && Main.instance.setmgr.getSettingByName("Send Message").getValue()) {
                mc.player.sendChatMessage("/homehome");
            }
        }
    }

    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}
