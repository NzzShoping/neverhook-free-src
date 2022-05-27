package ru.neverhook.feature.player;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;

public class AutoTool extends Feature {

    public AutoTool() {
        super("AutoTool", Category.Player);
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (!mc.gameSettings.keyBindAttack.pressed)
            return;
        if (mc.objectMouseOver == null)
            return;

        BlockPos blockPos = mc.objectMouseOver.getBlockPos();
        Block block = mc.world.getBlockState(blockPos).getBlock();
        float power = 1.0F;
        byte byt = -1;
        for (byte b1 = 0; b1 < 9; b1++) {
            ItemStack itemStack = mc.player.inventory.mainInventory.get(b1);
            ItemStack current = mc.player.inventory.getCurrentItem();
            if (itemStack != null && (itemStack.getStrVsBlock(block.getDefaultState())) > power && !(current.getStrVsBlock(block.getDefaultState()) > power)) {
                power = itemStack.getStrVsBlock(block.getDefaultState());
                byt = b1;
            }
        }
        if (byt != -1) {
            mc.player.inventory.currentItem = byt;
        }
    }
}

