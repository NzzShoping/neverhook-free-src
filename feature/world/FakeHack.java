package ru.neverhook.feature.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.combat.RotationUtil;
import ru.neverhook.utils.other.ChatUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class FakeHack extends Feature {

    public static ArrayList<String> fakeHackers = new ArrayList<String>();

    public float rot = 0;

    public FakeHack() {
        super("FakeHack", Category.World);
        Main.instance.setmgr.addSetting(new Setting("Hacker Attack Distance", this, 3, 1, 7, 1));
        Main.instance.setmgr.addSetting(new Setting("Hacker Sneak", this, false));
        Main.instance.setmgr.addSetting(new Setting("Hacker Spin", this, false));
    }


    public static boolean isFakeHacker(EntityPlayer player) {
        for (String name : fakeHackers) {
            EntityPlayer en = mc.world.getPlayerEntityByName(name);
            if (en == null) {
                continue;
            }
            if (player.isEntityEqual(en)) {
                return true;
            }
        }
        return false;
    }

    public static void removeHacker(EntityPlayer en) {
        Iterator<String> hackers = fakeHackers.iterator();
        while (hackers.hasNext()) {
            String name = hackers.next();
            if (mc.world.getPlayerEntityByName(name) == null) {
                continue;
            }
            if (en.isEntityEqual(Objects.requireNonNull(mc.world.getPlayerEntityByName(name)))) {
                Objects.requireNonNull(mc.world.getPlayerEntityByName(name)).setSneaking(false);
                hackers.remove();
            }
        }
    }

    @Override
    public void onDisable() {
        for (String name : fakeHackers) {
            if (Main.instance.setmgr.getSettingByName("Hacker Sneak").getValue()) {
                EntityPlayer player = mc.world.getPlayerEntityByName(name);
                assert player != null;
                player.setSneaking(false);
                player.setSprinting(false);
            }
        }
        super.onDisable();
    }

    @Override
    public void onEnable() {
        for (int i = 0; i < 3; i++) {
            ChatUtils.addChatMessage("To use this function write - " + ".fakehack (nick)");
        }
        fakeHackers.clear();
        super.onEnable();
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        for (String name : fakeHackers) {
            EntityPlayer player = mc.world.getPlayerEntityByName(name);
            if (player == null) {
                continue;
            }
            if (Main.instance.setmgr.getSettingByName("Hacker Sneak").getValue()) {
                player.setSneaking(true);
                player.setSprinting(true);
            } else {
                player.setSneaking(false);
                player.setSprinting(false);
            }
            float[] rots = RotationUtil.getFacePosEntityRemote(player, mc.player);
            float hackerReach = Main.instance.setmgr.getSettingByName("Hacker Attack Distance").getValInt();
            if (!Main.instance.setmgr.getSettingByName("Hacker Spin").getValue()) {
                if (player.getDistanceToEntity(mc.player) <= hackerReach) {
                    player.rotationYaw = rots[0];
                    player.rotationYawHead = rots[0];
                    player.rotationPitch = rots[1];
                }
            } else {
                float speed = 30;
                float yaw = ((float) (Math.floor(spinAim(speed))));
                player.rotationYaw = yaw;
                player.rotationYawHead = yaw;
            }
            if (mc.player.ticksExisted % 4 == 0 && player.getDistanceToEntity(mc.player) <= hackerReach) {
                player.swingArm(EnumHand.MAIN_HAND);
                if (mc.player.getDistanceToEntity(player) <= hackerReach) {
                    mc.player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 1.0F, 1.0F);
                }
            }
            if (mc.player.getDistanceToEntity(player) > hackerReach && !Main.instance.setmgr.getSettingByName("Hacker Sneak").getValue() && !Main.instance.setmgr.getSettingByName("Hacker Spin").getValue()) {
                float yaw = 75;
                player.rotationYaw = yaw;
                player.rotationPitch = 0;
                player.rotationYawHead = yaw;
            }
        }
    }

    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}
