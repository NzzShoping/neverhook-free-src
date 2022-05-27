package ru.neverhook.feature.other;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.util.ArrayList;

public class FastUse extends Feature {

    public FastUse() {
        super("FastUse", Category.Player);
        ArrayList<String> use = new ArrayList<>();
        use.add("Vanilla");
        Main.instance.setmgr.addSetting(new Setting("FastUse Mode", this, "Vanilla", use));
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("FastUse Mode").getValString();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Vanilla")) {
            if (mc.player.getItemInUseDuration() == 16 && (mc.player.isEating() || mc.player.isDrinking())) {
                for (int i = 0; i < 21; ++i) {
                    mc.player.connection.sendPacket(new CPacketPlayer(true));
                }
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        super.onDisable();
    }
}
