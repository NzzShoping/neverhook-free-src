package ru.neverhook.feature.visual;

import ru.neverhook.Main;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class ChatFeatures extends Feature {

    public static Setting clear;

    public ChatFeatures() {
        super("ChatFeatures", Category.Visuals);
        Main.instance.setmgr.addSetting(clear = new Setting("No Background", this, true));

    }

}
