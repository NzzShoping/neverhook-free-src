package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOn extends CommandBase {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "save-on";
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender) {
        return "commands.save-on.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        boolean flag = false;

        for (int i = 0; i < server.worldServers.length; ++i) {
            if (server.worldServers[i] != null) {
                WorldServer worldserver = server.worldServers[i];

                if (worldserver.disableLevelSaving) {
                    worldserver.disableLevelSaving = false;
                    flag = true;
                }
            }
        }

        if (flag) {
            notifyCommandListener(sender, this, "commands.save.enabled");
        } else {
            throw new CommandException("commands.save-on.alreadyOn");
        }
    }
}
