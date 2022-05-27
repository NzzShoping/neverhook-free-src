package ru.neverhook.feature.world;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventReceivePacket;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class AutoFish extends Feature {

    public AutoFish() {
        super("AutoFish", Category.World);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.NEUTRAL && packet.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH) {
                if (mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod || mc.player.getHeldItemOffhand().getItem() instanceof ItemFishingRod) {
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }
}
