package ru.neverhook.feature.player;

import net.minecraft.network.play.client.CPacketPlayer;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.util.ArrayList;

public class NoFall extends Feature {

    public NoFall() {
        super("NoFall", Category.Player);
        ArrayList<String> fall = new ArrayList<>();
        fall.add("Vanilla");
        fall.add("Matrix");
        Main.instance.setmgr.addSetting(new Setting("NoFall Mode", this, "Vanilla", fall));
    }

    @EventTarget
    public void onPreUpdate(EventPreMotionUpdate event) {
        if (mc.player == null || mc.world == null)
            return;
        String mode = Main.instance.setmgr.getSettingByName("NoFall Mode").getValString();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Vanilla")) {
            if (mc.player.fallDistance > 2) {
                mc.player.connection.sendPacket(new CPacketPlayer(true));
            }
        } else if (mode.equalsIgnoreCase("Matrix")) {
            if (mc.player.fallDistance >= 4) {
                event.setGround(mc.player.ticksExisted % 2 != 0);
                mc.player.fallDistance = 0;
            }
        }
    }
}
