package ru.neverhook.feature.visual;

import ru.neverhook.Main;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class Animations extends Feature {

    public static Setting speed;
    public static Setting autoblock;
    public static Setting item360;

    public Animations() {
        super("Animations", Category.Visuals);
        Main.instance.setmgr.addSetting(speed = new Setting("Swing Speed", this, 8, 1, 20, 1));
        Main.instance.setmgr.addSetting(autoblock = new Setting("Block Animation", this, true));
        Main.instance.setmgr.addSetting(item360 = new Setting("Item360", this, true));

    }
}
