package ru.neverhook.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import ru.neverhook.command.AbstractCommand;
import ru.neverhook.feature.world.FakeHack;
import ru.neverhook.ui.notification.NotificationManager;
import ru.neverhook.ui.notification.NotificationType;
import ru.neverhook.utils.other.ChatUtils;

public class FakeHackCommand extends AbstractCommand {

    public FakeHackCommand() {
        super("fakehack", "fakehack", "§6.fakehack" + ChatFormatting.LIGHT_PURPLE + " add | del | clear" + "§3<name>", "§6.fakehack" + ChatFormatting.LIGHT_PURPLE + " add | del | clear" + "§3<name>", "fakehack");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("fakehack")) {
                    if (arguments[1].equals("add")) {
                        FakeHack.fakeHackers.add(arguments[2]);
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Added" + " player " + ChatFormatting.RED + arguments[2] + ChatFormatting.WHITE + " as HACKER!");
                        NotificationManager.queue("FakeHack Manager", ChatFormatting.GREEN + "Added" + " player " + ChatFormatting.RED + arguments[2] + ChatFormatting.WHITE + " as HACKER!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("del")) {
                        EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByName(arguments[2]);
                        if (player == null) {
                            ChatUtils.addChatMessage("§cThat player could not be found!");
                            return;
                        }
                        if (FakeHack.isFakeHacker(player)) {
                            FakeHack.removeHacker(player);
                            ChatUtils.addChatMessage(ChatFormatting.GREEN + "Hacker " + ChatFormatting.RED + player.getName() + " " + ChatFormatting.WHITE + "was removed!");
                            NotificationManager.queue("FakeHack Manager", ChatFormatting.GREEN + "Hacker " + ChatFormatting.WHITE + "was removed!", 4, NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equals("clear")) {
                        if (FakeHack.fakeHackers.isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your FakeHack list is empty!");
                            NotificationManager.queue("FakeHack Manager", "Your FakeHack list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        FakeHack.fakeHackers.clear();
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Your FakeHack list " + ChatFormatting.WHITE + " successfully cleared!");
                        NotificationManager.queue("FakeHack Manager", ChatFormatting.GREEN + "Your FakeHack list " + ChatFormatting.WHITE + " successfully cleared!", 4, NotificationType.SUCCESS);
                    }
                }
            } else {
                ChatUtils.addChatMessage(getUsage());
            }
        } catch (Exception ignored) {

        }
    }
}
