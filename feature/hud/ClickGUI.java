package ru.neverhook.feature.hud;

import org.lwjgl.input.Keyboard;
import ru.neverhook.Main;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class ClickGUI extends Feature {

    public static Setting particles;
    public static Setting blur;
    public static Setting scrollInversion;

    public ClickGUI() {
        super("ClickGui", Category.Hud);
        Main.instance.setmgr.addSetting(particles = new Setting("Particles", this, true));
        Main.instance.setmgr.addSetting(blur = new Setting("Blur", this, false));
        Main.instance.setmgr.addSetting(scrollInversion = new Setting("ScrollInversion", this, true));
        setKey(Keyboard.KEY_RSHIFT);
    }

    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(Main.instance.clickGui);
        toggle();
    }
}
