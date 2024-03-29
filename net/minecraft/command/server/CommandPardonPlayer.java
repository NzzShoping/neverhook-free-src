package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandPardonPlayer extends CommandBase {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "pardon";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 3;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender) {
        return "commands.unban.usage";
    }

    /**
     * Check if the given ICommandSender has permission to execute this command
     */
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return server.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(server, sender);
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1 && args[0].length() > 0) {
            GameProfile gameprofile = server.getPlayerList().getBannedPlayers().getBannedProfile(args[0]);

            if (gameprofile == null) {
                throw new CommandException("commands.unban.failed", args[0]);
            } else {
                server.getPlayerList().getBannedPlayers().removeEntry(gameprofile);
                notifyCommandListener(sender, this, "commands.unban.success", args[0]);
            }
        } else {
            throw new WrongUsageException("commands.unban.usage");
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getPlayerList().getBannedPlayers().getKeys()) : Collections.emptyList();
    }
}
