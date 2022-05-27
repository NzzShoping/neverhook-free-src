package ru.neverhook.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import ru.neverhook.Main;
import ru.neverhook.api.friend.Friend;
import ru.neverhook.command.AbstractCommand;
import ru.neverhook.utils.file.impl.Friends;
import ru.neverhook.utils.other.ChatUtils;

import java.io.IOException;

public class FriendCommand extends AbstractCommand {

    public FriendCommand() {
        super("friend", "1", "§6.friend §3<name>", "friend add", "friend del", "friend");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length == 1) {
                ChatUtils.addChatMessage("§c.friend <name>");
                return;
            }
            String name = arguments[1];
            EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByName(name);
            if (player == null) {
                ChatUtils.addChatMessage("§cThat player could not be found!");
                return;
            }
            if (player == Minecraft.getMinecraft().player) {
                ChatUtils.addChatMessage("§cТы не можешь добавить самого себя!");
                return;
            }
            if (Main.instance.friendManager.isFriend(player.getName())) {
                Main.instance.friendManager.removeFriend(player.getName());
                ChatUtils.addChatMessage("§aRemoved player " + ChatFormatting.RED + name + ChatFormatting.WHITE + " §afrom friend list!");
            } else {
                Main.instance.friendManager.addFriend(new Friend(player.getName()));
                ChatUtils.addChatMessage("§aAdded player " + ChatFormatting.RED + name + ChatFormatting.WHITE + " §ato friend-list!");
            }

        } catch (Exception e) {
            ChatUtils.addChatMessage("§cNo, no, no. Usage: " + getUsage());
        }

        try {
            Main.getFileManager().getFile(Friends.class).saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
