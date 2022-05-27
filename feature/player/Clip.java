package ru.neverhook.feature.player;

import ru.neverhook.Main;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.util.ArrayList;

public class Clip extends Feature {

    public Clip() {
        super("Clip", Category.Player);
        ArrayList<String> options = new ArrayList<>();
        options.add("VClip");
        options.add("-VClip");
        options.add("HClip");
        Main.instance.setmgr.addSetting(new Setting("Clip Mode", this, "VClip", options));
        Main.instance.setmgr.addSetting(new Setting("Clip Power", this, 15, 1, 100, 1));
    }

    @Override
    public void onEnable() {

        double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;
        double yaw = mc.player.rotationYaw * 0.017453292;
        String mode = Main.instance.setmgr.getSettingByName("Clip Mode").getValString();

        if (mc.player == null && mc.world == null)
            return;

        float tp = Main.instance.setmgr.getSettingByName("Clip Power").getValFloat();

        if (mode.equalsIgnoreCase("VClip")) {
            mc.player.setPosition(x, y + tp, z);
        } else if (mode.equalsIgnoreCase("-VClip")) {
            mc.player.setPosition(x, y - tp, z);
        } else if (mode.equalsIgnoreCase("HClip")) {
            mc.player.setPosition(x - Math.sin(yaw) * tp, y, z + Math.cos(yaw) * tp);
        }

        super.onEnable();
    }
}
