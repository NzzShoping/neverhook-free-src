package ru.neverhook.feature.player;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.other.TimerUtils;

public class AntiTrapka extends Feature {

    private final Setting delay;
    private final Setting sneakCheck;
    private final Setting fallDist;
    TimerUtils timer = new TimerUtils();

    public AntiTrapka() {
        super("AntiTrapka", Category.Player);
        Main.instance.setmgr.addSetting(this.delay = new Setting("Delay", this, 5, 0, 50, 1));
        Main.instance.setmgr.addSetting(this.fallDist = new Setting("FallDistance", this, 3.3, 1.0, 30.0, 0.1));
        Main.instance.setmgr.addSetting(this.sneakCheck = new Setting("SneakCheck", this, true));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if ((mc.gameSettings.keyBindSneak.isKeyDown() && sneakCheck.getValue()))
            return;
        float checkUnder = fallDist.getValFloat();
        BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - checkUnder, mc.player.posZ);
        Block block = mc.world.getBlockState(blockPos).getBlock();
        if (Block.getIdFromBlock(block) == 58 || Block.getIdFromBlock(block) == 154 || Block.getIdFromBlock(block) == 107 || Block.getIdFromBlock(block) == 23 || Block.getIdFromBlock(block) == 30 || Block.getIdFromBlock(block) == 61
                || Block.getIdFromBlock(block) == 213 || Block.getIdFromBlock(block) == 131 || Block.getIdFromBlock(block) == 132) {
            float lg = delay.getValFloat();
            if (timer.hasReached(lg * 100)) {
                mc.player.jump();
                timer.reset();
            }
        }
    }
}
