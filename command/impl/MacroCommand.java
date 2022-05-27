package ru.neverhook.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import ru.neverhook.Main;
import ru.neverhook.command.AbstractCommand;
import ru.neverhook.ui.macro.Macro;
import ru.neverhook.ui.notification.NotificationManager;
import ru.neverhook.ui.notification.NotificationType;
import ru.neverhook.utils.other.ChatUtils;

public class MacroCommand extends AbstractCommand {

    public MacroCommand() {
        super("macros", "macros", "§6.macro" + ChatFormatting.LIGHT_PURPLE + " add " + "§3<key> </home_home> | §6.macro" + ChatFormatting.LIGHT_PURPLE + " del " + "§3<key> | §6.macro" + ChatFormatting.LIGHT_PURPLE + " clear " + "§3| §6.macro" + ChatFormatting.LIGHT_PURPLE + " list", "§6.macro" + ChatFormatting.LIGHT_PURPLE + " add " + "§3<key> </home_home> | §6.macro" + ChatFormatting.LIGHT_PURPLE + " del " + "§3<key> | §6.macro" + ChatFormatting.LIGHT_PURPLE + " clear " + "| §6.macro" + ChatFormatting.LIGHT_PURPLE + " list", "macro");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("macro")) {
                    if (arguments[1].equals("add")) {
                        String number = arguments[3];
                        Main.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(arguments[2].toUpperCase()), number.replace("_", " ")));
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Added" + " macro for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + "\"" + number.replace("_", " ") + "\"");
                        NotificationManager.queue("Macro Manager", ChatFormatting.GREEN + "Added" + " macro for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + "\"" + number.replace("_", " ") + "\"", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("clear")) {
                        if (Main.instance.macroManager.getMacros().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            NotificationManager.queue("Macro Manager", "Your macro list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        Main.instance.macroManager.getMacros().clear();
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Your macro list " + ChatFormatting.WHITE + " successfully cleared!");
                        NotificationManager.queue("Macro Manager", ChatFormatting.GREEN + "Your macro list " + ChatFormatting.WHITE + " successfully cleared!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("del")) {
                        Main.instance.macroManager.deleteMacroByKey(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"");
                        NotificationManager.queue("Macro Manager", ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("list")) {
                        if (Main.instance.macroManager.getMacros().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            NotificationManager.queue("Macro Manager", "Your macro list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        Main.instance.macroManager.getMacros().forEach(macro -> ChatUtils.addChatMessage(ChatFormatting.GREEN + "Macro list: " + ChatFormatting.WHITE + "Macro Name: " + ChatFormatting.RED + macro.getValue() + ChatFormatting.WHITE + ", Macro Bind: " + ChatFormatting.RED + Keyboard.getKeyName(macro.getKey())));
                    }
                }
            } else {
                ChatUtils.addChatMessage(getUsage());
            }
        } catch (Exception ignored) {

        }
    }
}
