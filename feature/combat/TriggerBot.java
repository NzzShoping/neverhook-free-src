package ru.neverhook.feature.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import ru.neverhook.Main;
import ru.neverhook.api.friend.Friend;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class TriggerBot extends Feature {

    public static Setting range;
    public static Setting players;
    public static Setting mobs;

    public TriggerBot() {
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !players.getValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !mobs.getValue()) {
                return false;
            }
            if (player instanceof EntityMob && !mobs.getValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !mobs.getValue()) {
                return false;
            }
        }
        return player != mc.player;
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        Entity entity = mc.objectMouseOver.entityHit;
        if (entity == null || mc.player.getDistanceToEntity(entity) > range.getValFloat() || entity instanceof EntityEnderCrystal || entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0.0f) {
            return;
        }

        if (canTrigger((EntityLivingBase) entity)) {
            if (mc.player.getCooledAttackStrength(0.0f) == 1.0f) {
                mc.playerController.attackEntity(mc.player, entity);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
}
