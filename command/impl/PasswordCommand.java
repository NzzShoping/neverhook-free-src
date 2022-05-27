package ru.neverhook.command.impl;

import ru.neverhook.command.AbstractCommand;
import ru.neverhook.feature.other.AutoAuth;
import ru.neverhook.utils.other.ChatUtils;

public class PasswordCommand extends AbstractCommand {

    public PasswordCommand() {
        super("password", "Change text displayed on watermark.", "§6.password §3<password]>", "password");
    }

    public void execute(String... arguments) {
        if (arguments.length >= 2) {
            StringBuilder string = new StringBuilder();

            for (int i = 1; i < arguments.length; ++i) {
                String tempString = arguments[i];
                tempString = tempString.replace('&', '§');
                string.append(tempString).append(" ");
            }

            ChatUtils.addChatMessage(String.format("§lChanged password to §f\"§a§l%s§f\" was §f\"§c§l%s§f\".", string.toString().trim(), AutoAuth.password));
            AutoAuth.password = string.toString().trim();
        } else {
            this.usage();
        }

    }
}