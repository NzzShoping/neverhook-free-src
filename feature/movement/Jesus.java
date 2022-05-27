package ru.neverhook.feature.movement;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.movement.MovementUtil;

import java.util.ArrayList;

public class Jesus extends Feature {

    public Setting boost;
    public Setting motionUp;

    public Jesus() {
        super("Jesus", Category.Movement);
        ArrayList<String> jesus = new ArrayList<>();
        jesus.add("MiniJump");
        jesus.add("Matrix Boost");
        Main.instance.setmgr.addSetting(new Setting("Jesus Mode", this, "MiniJump", jesus));
        Main.instance.setmgr.addSetting(boost = new Setting("Boost", this, 1, 0.1, 1.5, 0.1));
        Main.instance.setmgr.addSetting(motionUp = new Setting("Motion Up", this, 0.32, 0.3, 0.5, 0.01));
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("Jesus Mode").getValString();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("MiniJump")) {
            if (mc.player.isInWater() && mc.player.motionY < 0) {
                mc.player.jump();
                MovementUtil.setSpeed(0.3);
            }
        } else if (mode.equalsIgnoreCase("Matrix Boost")) {
            if (mc.player.isInLiquid() && mc.player.motionY < 0) {
                mc.player.motionY = motionUp.getValFloat();
                MovementUtil.setSpeed(boost.getValFloat());
            }
        }
    }
}

