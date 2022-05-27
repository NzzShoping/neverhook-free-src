package ru.neverhook.feature.player;

import ru.neverhook.Main;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class AntiPush extends Feature {

    public static Setting water;
    public static Setting players;
    public static Setting blocks;

    public AntiPush() {
        super("AntiPush", Category.Player);
        Main.instance.setmgr.addSetting(players = new Setting("Entity", this, true));
        Main.instance.setmgr.addSetting(water = new Setting("Liquid", this, true));
        Main.instance.setmgr.addSetting(blocks = new Setting("Blocks", this, true));
    }
}
