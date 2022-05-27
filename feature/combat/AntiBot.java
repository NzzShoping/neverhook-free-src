package ru.neverhook.feature.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventAttackClient;
import ru.neverhook.event.impl.EventReceivePacket;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.other.ChatUtils;

import java.util.ArrayList;

public class AntiBot extends Feature {

    public static ArrayList<Entity> isRealPlayer = new ArrayList();
    private boolean botCheck;

    public AntiBot() {
        super("AntiBot", Category.Combat);
        ArrayList<String> antibot = new ArrayList<>();
        antibot.add("Matrix");
        antibot.add("Need Hit");
        Main.instance.setmgr.addSetting(new Setting("AntiBot Mode", this, "Matrix", antibot));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("AntiBot Mode").getValString();
        this.setSuffix(mode);
    }

    @EventTarget
    public void onMouse(EventAttackClient event) {
        if (getState()) {
            String mode = Main.instance.setmgr.getSettingByName("AntiBot Mode").getValString();
            if (mode.equalsIgnoreCase("Need Hit")) {
                EntityPlayer entityPlayer = (EntityPlayer) mc.objectMouseOver.entityHit;
                String name = entityPlayer.getName();
                if (entityPlayer != null) {
                    if (Main.instance.friendManager.getFriends().contains(entityPlayer.getName()))
                        return;
                    if (isRealPlayer.contains(entityPlayer)) {
                        ChatUtils.addChatMessage(ChatFormatting.RED + name + ChatFormatting.WHITE + " Already in AntiBot-List!");
                    } else {
                        ChatUtils.addChatMessage(ChatFormatting.RED + name + ChatFormatting.WHITE + " Was added in AntiBot-List!");
                    }
                }
            }
        }
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        String mode = Main.instance.setmgr.getSettingByName("AntiBot Mode").getValString();
        if (mode.equalsIgnoreCase("Matrix")) {
            assert mc.getCurrentServerData() != null;
            if (!mc.getCurrentServerData().serverIP.equals("wellmore")) {
                if (event.getPacket() instanceof SPacketServerDifficulty) botCheck = false;
                SPacketPlayerListItem packetPlayerListItem = (SPacketPlayerListItem) event.getPacket();
                if (mc.player != null && event.getPacket() instanceof SPacketPlayerListItem && packetPlayerListItem.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                    String name = (packetPlayerListItem.getEntries().get(0)).getProfile().getName();
                    if (!botCheck) botCheck = name.equals(mc.player.getName());
                    else if (!mc.player.isSpectator() && !mc.player.capabilities.allowFlying) {
                        for (SPacketPlayerListItem.AddPlayerData ignored : ((SPacketPlayerListItem) event.getPacket()).getEntries()) {
                            if (!ignored.getGameMode().equals("NOT_SET") && ignored.getPing() != 0) {
                                event.setCancelled(true);
                                //NotificationPublisher.queue("AntiBot", "Bot was removed from the world!", 3, NotificationType.SUCCESS);
                            }
                        }
                    }
                }
            }
        }
    }
}
