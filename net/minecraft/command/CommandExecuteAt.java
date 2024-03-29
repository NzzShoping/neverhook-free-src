package net.minecraft.command;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandExecuteAt extends CommandBase {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "execute";
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
        return "commands.execute.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 5) {
            throw new WrongUsageException("commands.execute.usage");
        } else {
            Entity entity = getEntity(server, sender, args[0], Entity.class);
            double d0 = parseDouble(entity.posX, args[1], false);
            double d1 = parseDouble(entity.posY, args[2], false);
            double d2 = parseDouble(entity.posZ, args[3], false);
            new BlockPos(d0, d1, d2);
            int i = 4;

            if ("detect".equals(args[4]) && args.length > 10) {
                World world = entity.getEntityWorld();
                double d3 = parseDouble(d0, args[5], false);
                double d4 = parseDouble(d1, args[6], false);
                double d5 = parseDouble(d2, args[7], false);
                Block block = getBlockByText(sender, args[8]);
                BlockPos blockpos = new BlockPos(d3, d4, d5);

                if (!world.isBlockLoaded(blockpos)) {
                    throw new CommandException("commands.execute.failed", "detect", entity.getName());
                }

                IBlockState iblockstate = world.getBlockState(blockpos);

                if (iblockstate.getBlock() != block) {
                    throw new CommandException("commands.execute.failed", "detect", entity.getName());
                }

                if (!CommandBase.func_190791_b(block, args[9]).apply(iblockstate)) {
                    throw new CommandException("commands.execute.failed", "detect", entity.getName());
                }

                i = 10;
            }

            String s = buildString(args, i);
            ICommandSender icommandsender = CommandSenderWrapper.func_193998_a(sender).func_193997_a(entity, new Vec3d(d0, d1, d2)).func_194001_a(server.worldServers[0].getGameRules().getBoolean("commandBlockOutput"));
            ICommandManager icommandmanager = server.getCommandManager();

            try {
                int j = icommandmanager.executeCommand(icommandsender, s);

                if (j < 1) {
                    throw new CommandException("commands.execute.allInvocationsFailed", s);
                }
            } catch (Throwable var23) {
                throw new CommandException("commands.execute.failed", s, entity.getName());
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        } else if (args.length > 1 && args.length <= 4) {
            return getTabCompletionCoordinate(args, 1, pos);
        } else if (args.length > 5 && args.length <= 8 && "detect".equals(args[4])) {
            return getTabCompletionCoordinate(args, 5, pos);
        } else {
            return args.length == 9 && "detect".equals(args[4]) ? getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys()) : Collections.emptyList();
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
