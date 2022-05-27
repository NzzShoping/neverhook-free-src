package ru.neverhook.feature.visual;

import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.Event3D;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.visual.RenderUtil;

import java.awt.*;

public class BlockESP extends Feature {

    public static Setting enderChest;
    public static Setting chest;
    public static Setting clientColor;
    public static Setting chestColor;
    public static Setting enderChestColor;
    public static Setting shulkerColor;
    public static Setting bedColor;
    public static Setting spawnerColor;
    public static Setting espOutline;
    private final Setting bed;
    private final Setting shulker;
    private final Setting spawner;

    public BlockESP() {
        super("BlockESP", Category.Visuals);
        Main.instance.setmgr.addSetting(chest = new Setting("Chest", this, true));
        Main.instance.setmgr.addSetting(enderChest = new Setting("EnderChest", this, false));
        Main.instance.setmgr.addSetting(spawner = new Setting("Spawner", this, false));
        Main.instance.setmgr.addSetting(shulker = new Setting("Shulker", this, false));
        Main.instance.setmgr.addSetting(bed = new Setting("Bed", this, true));
        Main.instance.setmgr.addSetting(chestColor = new Setting("Chest Color", this, new Color(0xFFFFFF).getRGB(), true));
        Main.instance.setmgr.addSetting(enderChestColor = new Setting("EnderChest Color", this, new Color(0xFFFFFF).getRGB(), true));
        Main.instance.setmgr.addSetting(shulkerColor = new Setting("Shulker Color", this, new Color(0xFFFFFF).getRGB(), true));
        Main.instance.setmgr.addSetting(spawnerColor = new Setting("Spawner Color", this, new Color(0xFFFFFF).getRGB(), true));
        Main.instance.setmgr.addSetting(bedColor = new Setting("Bed Color", this, new Color(0xFFFFFF).getRGB(), true));
        Main.instance.setmgr.addSetting(espOutline = new Setting("ESP Outline", this, false));
        Main.instance.setmgr.addSetting(clientColor = new Setting("Client Colors", this, false));
    }

    @EventTarget
    public void onRender3D(Event3D event) {
        Color colorChest = clientColor.getValue() ? Main.getClientColor() : new Color(chestColor.getColorValue());
        Color enderColorChest = clientColor.getValue() ? Main.getClientColor() : new Color(enderChestColor.getColorValue());
        Color shulkColor = clientColor.getValue() ? Main.getClientColor() : new Color(shulkerColor.getColorValue());
        Color bedColoR = clientColor.getValue() ? Main.getClientColor() : new Color(bedColor.getColorValue());
        Color spawnerColoR = clientColor.getValue() ? Main.getClientColor() : new Color(spawnerColor.getColorValue());
        if (mc.player != null || mc.world != null) {
            for (TileEntity entity : mc.world.loadedTileEntityList) {
                BlockPos pos = entity.getPos();
                if (entity instanceof TileEntityChest && chest.getValue()) {
                    RenderUtil.blockEsp(pos, new Color(colorChest.getRGB()), 1, 1);
                } else if (entity instanceof TileEntityEnderChest && enderChest.getValue()) {
                    RenderUtil.blockEsp(pos, new Color(enderColorChest.getRGB()), 1, 1);
                } else if (entity instanceof TileEntityBed && bed.getValue()) {
                    RenderUtil.blockEsp(pos, new Color(bedColoR.getRGB()), 1, 1);
                } else if (entity instanceof TileEntityShulkerBox && shulker.getValue()) {
                    RenderUtil.blockEsp(pos, new Color(shulkColor.getRGB()), 1, 1);
                } else if (entity instanceof TileEntityMobSpawner && spawner.getValue()) {
                    RenderUtil.blockEsp(pos, new Color(spawnerColoR.getRGB()), 1, 1);
                }
            }
        }
    }
}
