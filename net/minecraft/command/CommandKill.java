package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandKill extends CommandBase {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "kill";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender) {
        return "commands.kill.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            EntityPlayer entityplayer = getCommandSenderAsPlayer(sender);
            entityplayer.onKillCommand();
            notifyCommandListener(sender, this, "commands.kill.successful", entityplayer.getDisplayName());
        } else {
            Entity entity = getEntity(server, sender, args[0]);
            entity.onKillCommand();
            notifyCommandListener(sender, this, "commands.kill.successful", entity.getDisplayName());
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }
}
