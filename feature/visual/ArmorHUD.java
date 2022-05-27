package ru.neverhook.feature.visual;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.Event2D;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.feature.hud.HUD;
import ru.neverhook.ui.clickgui.settings.Setting;

public class ArmorHUD extends Feature {

    private final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
    private final Setting showDur;

    public ArmorHUD() {
        super("ArmorHUD", Category.Visuals);
        Main.instance.setmgr.addSetting(this.showDur = new Setting("Show Durability", this, true));
    }

    @EventTarget
    public void onRender2D(Event2D event) {
        if (mc.player != null && mc.world != null) {
            renderArmorHUD();
        }
    }

    public void renderArmorHUD() {
        GlStateManager.enableTexture2D();
        ScaledResolution resolution = new ScaledResolution(mc);
        int i = resolution.getScaledWidth() / 2;
        int count = 0;
        int y = (int) (resolution.getScaledHeight() - 65 - (mc.player.isInsideOfMaterial(Material.WATER) ? HUD.globalOffset : HUD.globalOffset - 9));
        for (ItemStack is : mc.player.inventory.armorInventory) {
            count++;
            if (is.func_190926_b())
                continue;
            int x = i - 90 + (9 - count) * 20 + 2;
            GlStateManager.enableDepth();
            itemRender.zLevel = 200F;
            itemRender.renderItemAndEffectIntoGUI(is, x, y);
            if (this.showDur.getValue()) {
                itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, is, x, y, "");
            }
            itemRender.zLevel = 0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.func_190916_E() > 1 ? is.func_190916_E() + "" : "";
            mc.clickguismall.drawStringWithShadow(s, x + 19 - 2 - mc.fontRenderer.getStringWidth(s), y + 20, 0xffffff);
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
}
