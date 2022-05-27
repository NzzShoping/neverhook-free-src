package ru.neverhook.feature.world;

import ru.neverhook.Main;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class NoInteract extends Feature {

    public static Setting armorStands;

    public NoInteract() {
        super("NoInteract", Category.World);
        Main.instance.setmgr.addSetting(armorStands = new Setting("Armor Stands", this, true));
    }
}