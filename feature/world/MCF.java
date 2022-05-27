package ru.neverhook.feature.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import ru.neverhook.Main;
import ru.neverhook.api.friend.Friend;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventMouse;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.notification.NotificationManager;
import ru.neverhook.ui.notification.NotificationType;
import ru.neverhook.utils.file.impl.Friends;
import ru.neverhook.utils.other.ChatUtils;

import java.io.IOException;

public class MCF extends Feature {

    public MCF() {
        super("MCF", Category.Player);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.key == 2 && mc.pointedEntity instanceof EntityPlayer) {
            if (Main.instance.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(mc.pointedEntity.getName()))) {
                Main.instance.friendManager.getFriends().remove(Main.instance.friendManager.getFriend(mc.pointedEntity.getName()));
                ChatUtils.addChatMessage(ChatFormatting.RED + "Removed " + ChatFormatting.RESET + "'" + mc.pointedEntity.getName() + "'" + " as Friend!");
                NotificationManager.queue("MCF", "Removed " + "'" + mc.pointedEntity.getName() + "'" + " as Friend!", 4, NotificationType.INFO);
            } else {
                Main.instance.friendManager.addFriend(new Friend(mc.pointedEntity.getName()));
                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.RESET + mc.pointedEntity.getName() + " as Friend!");
                NotificationManager.queue("MCF", "Added " + mc.pointedEntity.getName() + " as Friend!", 4, NotificationType.SUCCESS);
            }
        }

        try {
            Main.getFileManager().getFile(Friends.class).saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

