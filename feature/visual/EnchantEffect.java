package ru.neverhook.feature.visual;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.Event2D;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.util.ArrayList;

public class EnchantEffect extends Feature {

    public static float hue = 0.0F;

    public EnchantEffect() {
        super("EnchantEffect", Category.Visuals);
        ArrayList<String> color = new ArrayList<>();
        color.add("Rgb");
        color.add("Custom");
        Main.instance.setmgr.addSetting(new Setting("Enchant color", this, "Rgb", color));
    }

    @EventTarget
    public void Render2d(Event2D e) {
        hue += 1f / 5.0f;
        if (hue > 255.0f) {
            hue = 0.0f;
        }
    }

}
