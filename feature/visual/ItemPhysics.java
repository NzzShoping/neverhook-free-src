package ru.neverhook.feature.visual;


import ru.neverhook.Main;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class ItemPhysics extends Feature {

    public static Setting physSpeed;

    public ItemPhysics() {
        super("ItemPhysics", Category.Visuals);
        Main.instance.setmgr.addSetting(physSpeed = new Setting("Physic Speed", this, 0.5, 0.1, 5, 0.1));
    }
}
