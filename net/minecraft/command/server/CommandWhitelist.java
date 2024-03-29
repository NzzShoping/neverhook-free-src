package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandWhitelist extends CommandBase {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "whitelist";
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
        return "commands.whitelist.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.whitelist.usage");
        } else {
            if ("on".equals(args[0])) {
                server.getPlayerList().setWhiteListEnabled(true);
                notifyCommandListener(sender, this, "commands.whitelist.enabled");
            } else if ("off".equals(args[0])) {
                server.getPlayerList().setWhiteListEnabled(false);
                notifyCommandListener(sender, this, "commands.whitelist.disabled");
            } else if ("list".equals(args[0])) {
                sender.addChatMessage(new TextComponentTranslation("commands.whitelist.list", server.getPlayerList().getWhitelistedPlayerNames().length, server.getPlayerList().getAvailablePlayerDat().length));
                String[] astring = server.getPlayerList().getWhitelistedPlayerNames();
                sender.addChatMessage(new TextComponentString(joinNiceString(astring)));
            } else if ("add".equals(args[0])) {
                if (args.length < 2) {
                    throw new WrongUsageException("commands.whitelist.add.usage");
                }

                GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[1]);

                if (gameprofile == null) {
                    throw new CommandException("commands.whitelist.add.failed", args[1]);
                }

                server.getPlayerList().addWhitelistedPlayer(gameprofile);
                notifyCommandListener(sender, this, "commands.whitelist.add.success", args[1]);
            } else if ("remove".equals(args[0])) {
                if (args.length < 2) {
                    throw new WrongUsageException("commands.whitelist.remove.usage");
                }

                GameProfile gameprofile1 = server.getPlayerList().getWhitelistedPlayers().getByName(args[1]);

                if (gameprofile1 == null) {
                    throw new CommandException("commands.whitelist.remove.failed", args[1]);
                }

                server.getPlayerList().removePlayerFromWhitelist(gameprofile1);
                notifyCommandListener(sender, this, "commands.whitelist.remove.success", args[1]);
            } else if ("reload".equals(args[0])) {
                server.getPlayerList().reloadWhitelist();
                notifyCommandListener(sender, this, "commands.whitelist.reloaded");
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "on", "off", "list", "add", "remove", "reload");
        } else {
            if (args.length == 2) {
                if ("remove".equals(args[0])) {
                    return getListOfStringsMatchingLastWord(args, server.getPlayerList().getWhitelistedPlayerNames());
                }

                if ("add".equals(args[0])) {
                    return getListOfStringsMatchingLastWord(args, server.getPlayerProfileCache().getUsernames());
                }
            }

            return Collections.emptyList();
        }
    }
}
