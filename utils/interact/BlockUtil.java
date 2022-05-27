package ru.neverhook.utils.interact;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import ru.neverhook.utils.other.MinecraftHelper;

import javax.vecmath.Vector3f;
import java.util.ArrayList;

public class BlockUtil implements MinecraftHelper {

    public static Vector3f getBlock(float radius, int block) {
        Vector3f tempVec = null;
        float dist = radius;
        for (float i = radius; i >= -radius; --i) {
            for (float j = -radius; j <= radius; ++j) {
                for (float k = radius; k >= -radius; --k) {
                    int posX = (int) (mc.player.posX + i);
                    int posY = (int) (mc.player.posY + j);
                    int posZ = (int) (mc.player.posZ + k);
                    float curDist = (float) mc.player.getDistance(posX, posY, posZ);
                    if (Block.getIdFromBlock(BlockUtil.getBlock(posX, posY - 1, posZ)) == block && BlockUtil.getBlock(posX, posY, posZ) instanceof BlockAir && curDist <= dist) {
                        dist = curDist;
                        tempVec = new Vector3f((float) posX, (float) posY, (float) posZ);
                    }
                }
            }
        }
        return tempVec;
    }

    public static ArrayList<BlockPos> getBlocks(int x, int y, int z) {
        BlockPos min = new BlockPos(mc.player.posX - x, mc.player.posY - y, mc.player.posZ - z);
        BlockPos max = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);

        return BlockUtil.getAllInBox(min, max);
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static IBlockState getState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    public static ArrayList<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
        ArrayList<BlockPos> blocks = new ArrayList<>();

        BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()),
                Math.min(from.getZ(), to.getZ()));
        BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()),
                Math.max(from.getZ(), to.getZ()));

        for (int x = min.getX(); x <= max.getX(); x++)
            for (int y = min.getY(); y <= max.getY(); y++)
                for (int z = min.getZ(); z <= max.getZ(); z++)
                    blocks.add(new BlockPos(x, y, z));

        return blocks;
    }

    public static Block getBlock(int x, int y, int z) {
        return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }
}
