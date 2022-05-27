package ru.neverhook.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import ru.neverhook.Main;
import ru.neverhook.command.AbstractCommand;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.notification.NotificationManager;
import ru.neverhook.ui.notification.NotificationType;
import ru.neverhook.utils.other.ChatUtils;

public class BindCommand extends AbstractCommand {

    public BindCommand() {
        super("bind", "bind", "§6.bind" + ChatFormatting.LIGHT_PURPLE + " add " + "§3<name> §3<key> | §6.bind " + ChatFormatting.LIGHT_PURPLE + "del " + "§3<name> <key>", "§6.bind" + ChatFormatting.LIGHT_PURPLE + " add " + "§3<name> §3<key> | §6.bind" + ChatFormatting.LIGHT_PURPLE + "del " + "§3<name> <key>", "bind");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length == 4) {
                String moduleName = arguments[2];
                String bind = arguments[3].toUpperCase();
                Feature feature = Main.instance.featureManager.getFeaturesByName(moduleName);
                if (arguments[0].equals("bind")) {
                    if (arguments[1].equals("add")) {
                        feature.setKey(Keyboard.getKeyIndex(bind));
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + feature.getName() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"");
                        NotificationManager.queue("Bind Manager", ChatFormatting.GREEN + feature.getName() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("del")) {
                        feature.setKey(0);
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + feature.getName() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"");
                        NotificationManager.queue("Bind Manager", ChatFormatting.GREEN + feature.getName() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"", 4, NotificationType.SUCCESS);
                    }
                }
            } else {
                ChatUtils.addChatMessage(getUsage());
            }
        } catch (Exception ignored) {

        }
    }
}
