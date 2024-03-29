package net.minecraft.command.server;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.*;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandTestForBlock extends CommandBase {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "testforblock";
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
        return "commands.testforblock.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 4) {
            throw new WrongUsageException("commands.testforblock.usage");
        } else {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockpos = parseBlockPos(sender, args, 0, false);
            Block block = getBlockByText(sender, args[3]);

            if (block == null) {
                throw new NumberInvalidException("commands.setblock.notFound", args[3]);
            } else {
                World world = sender.getEntityWorld();

                if (!world.isBlockLoaded(blockpos)) {
                    throw new CommandException("commands.testforblock.outOfWorld");
                } else {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    boolean flag = false;

                    if (args.length >= 6 && block.hasTileEntity()) {
                        String s = buildString(args, 5);

                        try {
                            nbttagcompound = JsonToNBT.getTagFromJson(s);
                            flag = true;
                        } catch (NBTException nbtexception) {
                            throw new CommandException("commands.setblock.tagError", nbtexception.getMessage());
                        }
                    }

                    IBlockState iblockstate = world.getBlockState(blockpos);
                    Block block1 = iblockstate.getBlock();

                    if (block1 != block) {
                        throw new CommandException("commands.testforblock.failed.tile", blockpos.getX(), blockpos.getY(), blockpos.getZ(), block1.getLocalizedName(), block.getLocalizedName());
                    } else if (args.length >= 5 && !CommandBase.func_190791_b(block, args[4]).apply(iblockstate)) {
                        try {
                            int i = iblockstate.getBlock().getMetaFromState(iblockstate);
                            throw new CommandException("commands.testforblock.failed.data", blockpos.getX(), blockpos.getY(), blockpos.getZ(), i, Integer.parseInt(args[4]));
                        } catch (NumberFormatException var13) {
                            throw new CommandException("commands.testforblock.failed.data", blockpos.getX(), blockpos.getY(), blockpos.getZ(), iblockstate.toString(), args[4]);
                        }
                    } else {
                        if (flag) {
                            TileEntity tileentity = world.getTileEntity(blockpos);

                            if (tileentity == null) {
                                throw new CommandException("commands.testforblock.failed.tileEntity", blockpos.getX(), blockpos.getY(), blockpos.getZ());
                            }

                            NBTTagCompound nbttagcompound1 = tileentity.writeToNBT(new NBTTagCompound());

                            if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true)) {
                                throw new CommandException("commands.testforblock.failed.nbt", blockpos.getX(), blockpos.getY(), blockpos.getZ());
                            }
                        }

                        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                        notifyCommandListener(sender, this, "commands.testforblock.success", blockpos.getX(), blockpos.getY(), blockpos.getZ());
                    }
                }
            }
        }
    }

    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length > 0 && args.length <= 3) {
            return getTabCompletionCoordinate(args, 0, pos);
        } else {
            return args.length == 4 ? getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys()) : Collections.emptyList();
        }
    }
}
