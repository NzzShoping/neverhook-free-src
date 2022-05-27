package ru.neverhook.feature.hud;

import ru.neverhook.Main;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class Notifications extends Feature {

    public static Setting state;

    public Notifications() {
        super("Notifications", Category.Hud);
        Main.instance.setmgr.addSetting(state = new Setting("Module State", this, true));
    }
}
