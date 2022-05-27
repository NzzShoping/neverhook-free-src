package ru.neverhook.command;

import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.SendMessageEvent;

public class CommandHandler {
    private final CommandManager commandManager;

    public CommandHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @EventTarget
    public void onMessage(SendMessageEvent event) {
        String msg = event.getMessage();
        if (msg.length() > 0 && msg.startsWith(".")) {
            event.setCancelled(this.commandManager.execute(msg));
        }

    }
}
