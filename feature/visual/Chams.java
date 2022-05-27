package ru.neverhook.feature.visual;

import ru.neverhook.Main;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.awt.*;

public class Chams extends Feature {

    public static Setting colorChams;
    public static Setting chamsAlpha;
    public static Setting clientColor;

    public Chams() {
        super("Chams", Category.Visuals);
        Main.instance.setmgr.addSetting(colorChams = new Setting("Chams Color", this, new Color(0xFFFFFF).getRGB(), true));
        Main.instance.setmgr.addSetting(chamsAlpha = new Setting("Chams Alpha", this, 0.2F, 0.2, 1F, 0.1F));
        Main.instance.setmgr.addSetting(clientColor = new Setting("Client Colored", this, false));
    }
}
