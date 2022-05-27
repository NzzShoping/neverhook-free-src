package ru.neverhook.utils.combat;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import ru.neverhook.Main;
import ru.neverhook.api.friend.Friend;
import ru.neverhook.feature.combat.AntiBot;
import ru.neverhook.feature.combat.KillAura;
import ru.neverhook.ui.notification.NotificationManager;
import ru.neverhook.ui.notification.NotificationType;
import ru.neverhook.utils.other.MinecraftHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KillauraUtil implements MinecraftHelper {

    public static boolean canAttack(EntityLivingBase player) {
        String mode = Main.instance.setmgr.getSettingByName("AntiBot Mode").getValString();
        for (Friend friend : Main.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }

        if (Main.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && !AntiBot.isRealPlayer.contains(player) && (mode.equalsIgnoreCase("Need Hit"))) {
            return false;
        }

        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !KillAura.players.getValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !KillAura.mobs.getValue()) {
                return false;
            }
            if (player instanceof EntityMob && !KillAura.mobs.getValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !KillAura.mobs.getValue()) {
                return false;
            }
        }
        if (player.isInvisible() && !KillAura.invis.getValue()) {
            return false;
        }
        if (player instanceof EntityArmorStand) {
            return false;
        }
        if (!canSeeEntityAtFov(player, KillAura.fov.getValFloat())) {
            return false;
        }
        if (!range(player, KillAura.range.getValFloat())) {
            return false;
        }
        if (!player.canEntityBeSeen(mc.player)) {
            return KillAura.walls.getValue();
        }
        return player != mc.player;
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffX = entityLiving.posX - mc.player.posX;
        double diffZ = entityLiving.posZ - mc.player.posZ;
        float newYaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = angleDifference(newYaw, mc.player.rotationYaw);
        return difference <= scope;
    }

    public static double angleDifference(double a, double b) {
        float yaw360 = (float) (Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }

    private static boolean range(EntityLivingBase entity, double range) {
        return mc.player.getDistanceToEntity(entity) <= range;
    }

    public static void toggleOffChecks() {
        KillAura killAura = (KillAura) Main.instance.featureManager.getFeatureByClass(KillAura.class);
        if (mc.currentScreen instanceof GuiGameOver && KillAura.target.isDead) {
            killAura.toggle();
            NotificationManager.queue(killAura.getName(), "Killaura was toggled off!", 2, NotificationType.SUCCESS);
            return;
        }
        if (mc.player.ticksExisted <= 1) {
            killAura.toggle();
            NotificationManager.queue(killAura.getName(), "Killaura was toggled off!", 2, NotificationType.SUCCESS);
        }
    }

    public static EntityLivingBase getSortEntities() {
        List<EntityLivingBase> entity = new ArrayList<>();
        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) e;
                if (mc.player.getDistanceToEntity(player) < KillAura.range.getValFloat() && (canAttack(player))) {
                    entity.add(player);
                }
            }
        }

        String sortMode = Main.instance.setmgr.getSettingByName("TargetSort Mode").getValString();
        if (sortMode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
        } else if (sortMode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble(mc.player::getDistanceToEntity));
        }

        if (entity.isEmpty())
            return null;

        return entity.get(0);
    }
}
