package ru.neverhook.utils.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemShield;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import ru.neverhook.utils.other.MinecraftHelper;

public class EntityUtil implements MinecraftHelper {

    public static void block() {
        if (!mc.player.isBlocking() && mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemShield) {
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
        }
    }

    public static void attackEntity(Entity entity, boolean packet, boolean swingArm) {
        if (packet) {
            EntityUtil.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
        } else {
            EntityUtil.mc.playerController.attackEntity(EntityUtil.mc.player, entity);
        }
        if (swingArm) {
            EntityUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        } else {
            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
    }

    public static double getDistance(double x, double y, double z, double x1, double y1, double z1) {
        double posX = x - x1;
        double posY = y - y1;
        double posZ = z - z1;
        return MathHelper.sqrt(posX * posX + posY * posY + posZ * posZ);
    }

    public static float clamp(double val, double min, double max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return (float) val;
    }
}