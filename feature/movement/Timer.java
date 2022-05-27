package ru.neverhook.feature.movement;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class Timer extends Feature {

    public Timer() {
        super("Timer", Category.Movement);
        Main.instance.setmgr.addSetting(new Setting("Timer", this, 1.0, 0.1, 10, 0.1));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!getState())
            return;
        this.setSuffix("" + Main.instance.setmgr.getSettingByName("Timer").getValFloat());
        mc.timer.timerSpeed = Main.instance.setmgr.getSettingByName("Timer").getValFloat();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.timer.timerSpeed = 1.0f;
    }
}
