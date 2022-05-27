package ru.neverhook.feature.visual;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventReceivePacket;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.util.ArrayList;

public class Ambience extends Feature {

    private final Setting time;
    private long lol = 0;

    public Ambience() {
        super("Ambience", Category.Visuals);
        ArrayList<String> ambi = new ArrayList<>();
        ambi.add("Day");
        ambi.add("Night");
        ambi.add("Morning");
        ambi.add("Sunset");
        ambi.add("Spin");
        Main.instance.setmgr.addSetting(new Setting("Ambience Mode", this, "Night", ambi));
        Main.instance.setmgr.addSetting(this.time = new Setting("TimeSpin Speed", this, 20.0D, 0.1D, 1000.0D, 1));
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("Ambience Mode").getValString();
        if (mode.equalsIgnoreCase("Spin")) {
            mc.world.setWorldTime(lol);
            this.lol = lol + time.getValLong();
        } else if (mode.equalsIgnoreCase("Day")) {
            mc.world.setWorldTime(5000);
        } else if (mode.equalsIgnoreCase("Night")) {
            mc.world.setWorldTime(17000);
        } else if (mode.equalsIgnoreCase("Morning")) {
            mc.world.setWorldTime(0);
        } else if (mode.equalsIgnoreCase("Sunset")) {
            mc.world.setWorldTime(13000);
        }
    }
}
