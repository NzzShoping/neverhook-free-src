package ru.neverhook.feature.visual;

import net.minecraft.potion.Potion;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class NoRender extends Feature {

    public static Setting rain;
    public static Setting hurt;
    public static Setting pumpkin;
    public static Setting armor;
    public static Setting totem;
    public static Setting blindness;
    public static Setting liquidOverlay;
    public static Setting cameraSmooth;
    public static Setting fire;
    public static Setting walk;
    public static Setting arrow;

    public NoRender() {
        super("NoRender", Category.Visuals);
        Main.instance.setmgr.addSetting(rain = new Setting("Rain", this, true));
        Main.instance.setmgr.addSetting(hurt = new Setting("HurtCamera", this, true));
        Main.instance.setmgr.addSetting(pumpkin = new Setting("Pumpkin", this, true));
        Main.instance.setmgr.addSetting(armor = new Setting("Armor", this, false));
        Main.instance.setmgr.addSetting(totem = new Setting("Totem", this, true));
        Main.instance.setmgr.addSetting(blindness = new Setting("Blindness", this, true));
        Main.instance.setmgr.addSetting(liquidOverlay = new Setting("Liquid Overlay", this, true));
        Main.instance.setmgr.addSetting(cameraSmooth = new Setting("Camera Smooth", this, true));
        Main.instance.setmgr.addSetting(fire = new Setting("Fire", this, true));
        Main.instance.setmgr.addSetting(walk = new Setting("Walk", this, false));
        Main.instance.setmgr.addSetting(arrow = new Setting("Arrow", this, true));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (cameraSmooth.getValue() && this.getState()) {
            mc.gameSettings.smoothCamera = false;
            if (rain.getValue() && mc.world.isRaining()) {
                mc.world.setRainStrength(0);
                mc.world.setThunderStrength(0);
            }
            if (blindness.getValue() && mc.player.isPotionActive(Potion.getPotionById(15))) {
                mc.player.removeActivePotionEffect(Potion.getPotionById(15));
            }
        }
    }
}
