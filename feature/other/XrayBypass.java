package ru.neverhook.feature.other;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.Event2D;
import ru.neverhook.event.impl.Event3D;
import ru.neverhook.event.impl.EventPreMotionUpdate;
import ru.neverhook.event.impl.EventReceivePacket;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.font.FontRenderer;
import ru.neverhook.utils.interact.BlockUtil;
import ru.neverhook.utils.visual.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

public class XrayBypass extends Feature {

    public static int done;
    public static int all;
    private final Setting diamond;
    private final Setting gold;
    private final Setting iron;
    private final Setting emerald;
    private final Setting redstone;
    private final Setting lapis;
    private final Setting coal;
    private final Setting checkSpeed;
    ArrayList<BlockPos> ores = new ArrayList<>();
    ArrayList<BlockPos> toCheck = new ArrayList<>();

    public XrayBypass() {
        super("XrayBypass", Category.World);
        ArrayList<String> options = new ArrayList<>();
        options.add("FullBox");
        options.add("Frame");
        Main.instance.setmgr.rSetting(new Setting("BlockOutline Mode", this, "FullBox", options));
        Main.instance.setmgr.rSetting(this.diamond = new Setting("Diamond", this, true));
        Main.instance.setmgr.rSetting(this.gold = new Setting("Gold", this, false));
        Main.instance.setmgr.rSetting(this.iron = new Setting("Iron", this, false));
        Main.instance.setmgr.rSetting(this.emerald = new Setting("Emerald", this, false));
        Main.instance.setmgr.rSetting(this.redstone = new Setting("Redstone", this, false));
        Main.instance.setmgr.rSetting(this.lapis = new Setting("Lapis", this, false));
        Main.instance.setmgr.rSetting(this.coal = new Setting("Coal", this, false));
        Main.instance.setmgr.rSetting(this.checkSpeed = new Setting("CheckSpeed", this, 4, 1, 5, 1));
        Main.instance.setmgr.rSetting(new Setting("Radius XZ", this, 20, 5, 200, 1));
        Main.instance.setmgr.rSetting(new Setting("Radius Y", this, 6, 2, 50, 1));
    }

    @Override
    public void onEnable() {
        ores.clear();
        toCheck.clear();

        int radXZ = Main.instance.setmgr.getSettingByName("Radius XZ").getValInt();
        int radY = Main.instance.setmgr.getSettingByName("Radius Y").getValInt();

        ArrayList<BlockPos> blockPositions = getBlocks(radXZ, radY, radXZ);

        for (BlockPos pos : blockPositions) {
            IBlockState state = BlockUtil.getState(pos);
            if (isCheckableOre(Block.getIdFromBlock(state.getBlock()))) {
                toCheck.add(pos);
            }
        }

        all = toCheck.size();
        done = 0;

        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.renderGlobal.loadRenderers();
        super.onDisable();
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate e) {
        String a = done == all ? "" + "Done: " + all : "" + done;
        String mode = Main.instance.setmgr.getSettingByName("BlockOutline Mode").getValString();
        this.setSuffix(mode + ", " + a);
        for (int i = 0; i < checkSpeed.getValInt(); i++) {
            if (toCheck.size() < 1)
                return;
            BlockPos pos = toCheck.remove(0);
            done++;
            mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
        if (e.getPacket() instanceof SPacketBlockChange) {
            SPacketBlockChange p = (SPacketBlockChange) e.getPacket();

            if (isEnabledOre(Block.getIdFromBlock(p.getBlockState().getBlock()))) {
                ores.add(p.getBlockPosition());
            }
        } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
            SPacketMultiBlockChange p = (SPacketMultiBlockChange) e.getPacket();

            for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
                if (isEnabledOre(Block.getIdFromBlock(dat.getBlockState().getBlock()))) {
                    ores.add(dat.getPos());

                }
            }
        }
    }

    @EventTarget
    public void onRender2D(Event2D e) {
        ScaledResolution sr = new ScaledResolution(mc);
        FontRenderer font = mc.verdana;
        int size = 125;
        float xOffset = (sr.getScaledWidth() / 2F) - (size / 2F);
        float yOffset = 5;
        Main.getFontRender().drawStringWithOutline("" + "Done: " + XrayBypass.done + " / " + "All: " + XrayBypass.all, xOffset + 30F, yOffset + font.getHeight() + 4F, -1);
        GlStateManager.disableBlend();
    }

    @EventTarget
    public void onRender3D(Event3D e) {
        for (BlockPos pos : ores) {
            IBlockState state = BlockUtil.getState(pos);
            Block mat = state.getBlock();
            String mode = Main.instance.setmgr.getSettingByName("BlockOutline Mode").getValString();
            if (mode.equalsIgnoreCase("FullBox")) {
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 56 && diamond.getValue()) {
                    if (Block.getIdFromBlock(mat) == 56) {
                        RenderUtil.blockEsp(pos, new Color(0, 255, 255, 50), 1, 1);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 14 && gold.getValue()) {
                    if (Block.getIdFromBlock(mat) == 14) {
                        RenderUtil.blockEsp(pos, new Color(255, 215, 0, 100), 1, 1);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 15 && iron.getValue()) {
                    if (Block.getIdFromBlock(mat) == 15) {
                        RenderUtil.blockEsp(pos, new Color(213, 213, 213, 100), 1, 1);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 129 && emerald.getValue()) {
                    if (Block.getIdFromBlock(mat) == 129) {
                        RenderUtil.blockEsp(pos, new Color(0, 255, 77, 100), 1, 1);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 73 && redstone.getValue()) {
                    if (Block.getIdFromBlock(mat) == 73) {
                        RenderUtil.blockEsp(pos, new Color(255, 0, 0, 100), 1, 1);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 16 && coal.getValue()) {
                    if (Block.getIdFromBlock(mat) == 16) {
                        RenderUtil.blockEsp(pos, new Color(0, 0, 0, 100), 1, 1);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 21 && lapis.getValue()) {
                    if (Block.getIdFromBlock(mat) == 21) {
                        RenderUtil.blockEsp(pos, new Color(38, 97, 156, 100), 1, 1);
                    }
                }
            } else if (mode.equalsIgnoreCase("Frame")) {
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 56 && diamond.getValue()) {
                    if (Block.getIdFromBlock(mat) == 56) {
                        RenderUtil.blockEspFrame(pos, 0, 255, 255);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 14 && gold.getValue()) {
                    if (Block.getIdFromBlock(mat) == 14) {
                        RenderUtil.blockEspFrame(pos, 255, 215, 0);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 15 && iron.getValue()) {
                    if (Block.getIdFromBlock(mat) == 15) {
                        RenderUtil.blockEspFrame(pos, 213, 213, 213);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 129 && emerald.getValue()) {
                    if (Block.getIdFromBlock(mat) == 129) {
                        RenderUtil.blockEspFrame(pos, 0, 255, 77);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 73 && redstone.getValue()) {
                    if (Block.getIdFromBlock(mat) == 73) {
                        RenderUtil.blockEspFrame(pos, 255, 0, 0);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 16 && coal.getValue()) {
                    if (Block.getIdFromBlock(mat) == 16) {
                        RenderUtil.blockEspFrame(pos, 0, 0, 0);
                    }
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 21 && lapis.getValue()) {
                    if (Block.getIdFromBlock(mat) == 21) {
                        RenderUtil.blockEspFrame(pos, 38, 97, 156);
                    }
                }
            }
        }
    }

    private boolean isCheckableOre(int id) {
        int check = 0;
        int check1 = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        if (diamond.getValue() && id != 0) {
            check = 56;
        }
        if (gold.getValue() && id != 0) {
            check1 = 14;
        }
        if (iron.getValue() && id != 0) {
            check2 = 15;
        }
        if (emerald.getValue() && id != 0) {
            check3 = 129;
        }
        if (redstone.getValue() && id != 0) {
            check4 = 73;
        }
        if (coal.getValue() && id != 0) {
            check5 = 16;
        }
        if (lapis.getValue() && id != 0) {
            check6 = 21;
        }
        if (id == 0) {
            return false;
        }
        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6;
    }

    private boolean isEnabledOre(int id) {
        int check = 0;
        int check1 = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        if (diamond.getValue() && id != 0) {
            check = 56;
        }
        if (gold.getValue() && id != 0) {
            check1 = 14;
        }
        if (iron.getValue() && id != 0) {
            check2 = 15;
        }
        if (emerald.getValue() && id != 0) {
            check3 = 129;
        }
        if (redstone.getValue() && id != 0) {
            check4 = 73;
        }
        if (coal.getValue() && id != 0) {
            check5 = 16;
        }
        if (lapis.getValue() && id != 0) {
            check6 = 21;
        }
        if (id == 0) {
            return false;
        }
        return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6;
    }

    private ArrayList<BlockPos> getBlocks(int x, int y, int z) {
        BlockPos min = new BlockPos(mc.player.posX - x, mc.player.posY - y, mc.player.posZ - z);
        BlockPos max = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);

        return BlockUtil.getAllInBox(min, max);
    }

}
