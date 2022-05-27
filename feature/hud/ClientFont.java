package ru.neverhook.feature.hud;

import ru.neverhook.Main;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.util.ArrayList;

public class ClientFont extends Feature {

    public static Setting defaultFont;

    public ClientFont() {
        super("Client Font", Category.Hud);
        ArrayList<String> fontList = new ArrayList<>();
        fontList.add("Comfortaa");
        fontList.add("SF UI");
        fontList.add("Verdana");
        fontList.add("RobotoLight");
        fontList.add("RobotoRegular");
        fontList.add("Lato");
        Main.instance.setmgr.addSetting(new Setting("FontList", this, "Comfortaa", fontList));
        Main.instance.setmgr.addSetting(defaultFont = new Setting("Minecraft Font", this, false));
    }

    @Override
    public void onEnable() {
        toggle();
        super.onEnable();
    }
}
