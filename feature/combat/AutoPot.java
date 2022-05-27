package ru.neverhook.feature.combat;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.notification.NotificationManager;
import ru.neverhook.ui.notification.NotificationType;
import ru.neverhook.utils.inventory.InventoryUtil;

public class AutoPot extends Feature {

    private int item;
    private int oldSlot;
    private int ticks;

    public AutoPot() {
        super("AutoPot", Category.Combat);
    }

    @Override
    public void onDisable() {
        this.ticks = 0;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        NotificationManager.queue("AutoPot", "Can only be stable on Wellmore!", 4, NotificationType.WARNING);
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        if (!doesNextSlotHavePot()) {
            return;
        }

        item = InventoryUtil.getSlotWithPot();
        oldSlot = mc.player.inventory.currentItem;

        ticks++;

        if (!mc.player.onGround) return;

        if (this.ticks != 0 && !(mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && mc.player.isPotionActive(MobEffects.STRENGTH) && mc.player.isPotionActive(MobEffects.SPEED))) {
            event.setPitch(MathHelper.clamp(90, -90, 90));
        }

        if (!(mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)) {
            if (ticks > 5) {
                if (!mc.player.isPotionActive(MobEffects.SPEED)) {
                    switchNextPot(event, 0);
                } else if (mc.player.isPotionActive(MobEffects.SPEED) && !mc.player.isPotionActive(MobEffects.STRENGTH)) {
                    switchNextPot(event, 1);
                } else if (mc.player.isPotionActive(MobEffects.STRENGTH) && !mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    if (event.getPitch() == 90) {
                        switchNextPot(event, 2);
                    }
                } else {
                    this.ticks = 0;
                }
            }
        } else {
            if (ticks > 5) {
                if (!mc.player.isPotionActive(MobEffects.STRENGTH)) {
                    switchNextPot(event, 0);
                } else if (mc.player.isPotionActive(MobEffects.STRENGTH) && !mc.player.isPotionActive(MobEffects.SPEED)) {
                    switchNextPot(event, 1);
                } else if (mc.player.isPotionActive(MobEffects.SPEED) && !mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    switchNextPot(event, 2);
                } else {
                    this.ticks = 0;
                }
            }
        }
    }

    private void switchNextPot(EventPreMotionUpdate event, int slot) {
        if (event.getPitch() == 90) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(this.item + slot));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
        }
    }

    private boolean doesNextSlotHavePot() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION) {
                return true;
            }
        }
        return false;
    }
}
