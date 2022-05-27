package ru.neverhook.feature.hud;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.Event2D;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.awt.*;
import java.util.ArrayList;

public class ArreyList extends Feature {

    public static Setting backGround;
    public static Setting border;
    public static Setting rectRight;
    public static Setting onecolor;
    public static Setting twocolor;
    public static Setting time;
    public static Setting x;
    public static Setting y;
    public static Setting height;
    public static Setting size;

    public ArreyList() {
        super("ArrayList", Category.Hud);

        /* COLOR SETTINGS */

        ArrayList<String> color = new ArrayList<>();
        color.add("Custom");
        color.add("Rainbow");
        color.add("Pulse");
        color.add("Astolfo");
        color.add("None");
        color.add("Category");
        Main.instance.setmgr.addSetting(new Setting("ArrayList Color", this, "Astolfo", color));

        /* OTHER */

        Main.instance.setmgr.addSetting(backGround = new Setting("Background", this, true));
        Main.instance.setmgr.addSetting(border = new Setting("Border", this, true));
        Main.instance.setmgr.addSetting(rectRight = new Setting("RectRight", this, true));
        Main.instance.setmgr.addSetting(new Setting("BackgroundAplha", this, 1, 1.0, 255, 1));
        Main.instance.setmgr.addSetting(new Setting("BackgroundBright", this, 255, 1.0, 255, 1));
        Main.instance.setmgr.addSetting(onecolor = new Setting("One Color", this, new Color(0xFFFFFF).getRGB(), true));
        Main.instance.setmgr.addSetting(twocolor = new Setting("Two Color", this, new Color(0xFF0000).getRGB(), true));
        Main.instance.setmgr.addSetting(time = new Setting("Color Time", this, 10, 1, 100, 1));
        Main.instance.setmgr.addSetting(x = new Setting("ArrayList X", this, 0, 0, 500, 0.1f));
        Main.instance.setmgr.addSetting(y = new Setting("ArrayList Y", this, 0, 0, 500, 0.1f));
        Main.instance.setmgr.addSetting(height = new Setting("Font Height", this, 10, 7, 20, 1));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("ArrayList Color").getValString();
        setSuffix(mode);
    }

    @EventTarget
    public void onRender2D(Event2D event) {
        HUD.onRenderArrayList(event.getResolution());
    }
}