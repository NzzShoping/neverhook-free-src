package ru.neverhook.feature.visual;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.util.ArrayList;
import java.util.Objects;

public class FullBright extends Feature {

    public FullBright() {
        super("FullBright", Category.Visuals);
        ArrayList<String> bright = new ArrayList<>();
        bright.add("Gamma");
        bright.add("Potion");
        Main.instance.setmgr.addSetting(new Setting("FullBright Mode", this, "Gamma", bright));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (this.getState()) {
            String mode = Main.instance.setmgr.getSettingByName("FullBright Mode").getValString();
            if (mode.equalsIgnoreCase("Gamma")) {
                mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(16)));
                mc.gameSettings.gammaSetting = 1000F;
            } else if (mode.equalsIgnoreCase("Potion")) {
                mc.player.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(16)), 817, 1));
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = 0.1F;
        mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(16)));
    }
}
