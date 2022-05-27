package ru.neverhook.command.impl;

import ru.neverhook.command.AbstractCommand;
import ru.neverhook.feature.hud.HUD;
import ru.neverhook.utils.other.ChatUtils;

public class ClientNameCommand extends AbstractCommand {

    public ClientNameCommand() {
        super("clientname", "Change text displayed on watermark.", "§6.clientname §3<name>", "clientname", "name", "rename");
    }

    public void execute(String... arguments) {
        if (arguments.length >= 2) {
            StringBuilder string = new StringBuilder();

            for (int i = 1; i < arguments.length; ++i) {
                String tempString = arguments[i];
                tempString = tempString.replace('&', '§');
                string.append(tempString).append(" ");
            }

            ChatUtils.addChatMessage(String.format("§lChanged client name to §f\"§a§l%s§f\" was §f\"§c§l%s§f\".", string.toString().trim(), HUD.clientName));
            HUD.clientName = string.toString().trim();
        } else {
            this.usage();
        }

    }
}