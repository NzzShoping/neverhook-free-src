package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandTime extends CommandBase {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "time";
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
        return "commands.time.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 1) {
            if ("set".equals(args[0])) {
                int i1;

                if ("day".equals(args[1])) {
                    i1 = 1000;
                } else if ("night".equals(args[1])) {
                    i1 = 13000;
                } else {
                    i1 = parseInt(args[1], 0);
                }

                this.setAllWorldTimes(server, i1);
                notifyCommandListener(sender, this, "commands.time.set", i1);
                return;
            }

            if ("add".equals(args[0])) {
                int l = parseInt(args[1], 0);
                this.incrementAllWorldTimes(server, l);
                notifyCommandListener(sender, this, "commands.time.added", l);
                return;
            }

            if ("query".equals(args[0])) {
                if ("daytime".equals(args[1])) {
                    int k = (int) (sender.getEntityWorld().getWorldTime() % 24000L);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, k);
                    notifyCommandListener(sender, this, "commands.time.query", k);
                    return;
                }

                if ("day".equals(args[1])) {
                    int j = (int) (sender.getEntityWorld().getWorldTime() / 24000L % 2147483647L);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j);
                    notifyCommandListener(sender, this, "commands.time.query", j);
                    return;
                }

                if ("gametime".equals(args[1])) {
                    int i = (int) (sender.getEntityWorld().getTotalWorldTime() % 2147483647L);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
                    notifyCommandListener(sender, this, "commands.time.query", i);
                    return;
                }
            }
        }

        throw new WrongUsageException("commands.time.usage");
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "set", "add", "query");
        } else if (args.length == 2 && "set".equals(args[0])) {
            return getListOfStringsMatchingLastWord(args, "day", "night");
        } else {
            return args.length == 2 && "query".equals(args[0]) ? getListOfStringsMatchingLastWord(args, "daytime", "gametime", "day") : Collections.emptyList();
        }
    }

    protected void setAllWorldTimes(MinecraftServer server, int time) {
        for (int i = 0; i < server.worldServers.length; ++i) {
            server.worldServers[i].setWorldTime(time);
        }
    }

    protected void incrementAllWorldTimes(MinecraftServer server, int amount) {
        for (int i = 0; i < server.worldServers.length; ++i) {
            WorldServer worldserver = server.worldServers[i];
            worldserver.setWorldTime(worldserver.getWorldTime() + (long) amount);
        }
    }
}
