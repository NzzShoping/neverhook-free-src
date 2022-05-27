package ru.neverhook.feature.combat;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class FastBow extends Feature {

    private final Setting ticks;

    public FastBow() {
        super("FastBow", Category.Combat);
        Main.instance.setmgr.addSetting(this.ticks = new Setting("Bow Ticks", this, 1.5, 1.5, 10, 0.5));
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow) {
            if (mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= ticks.getValFloat()) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mc.player.stopActiveHand();
            }
        }
    }
}
