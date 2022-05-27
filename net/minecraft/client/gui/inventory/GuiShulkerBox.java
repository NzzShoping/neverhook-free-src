package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import ru.neverhook.Main;

import java.awt.*;

public class GuiShulkerBox extends GuiContainer {
    private static final ResourceLocation field_190778_u = new ResourceLocation("textures/gui/container/shulker_box.png");
    private final IInventory field_190779_v;
    private final InventoryPlayer field_190780_w;
    private float curAlpha;

    public GuiShulkerBox(InventoryPlayer p_i47233_1_, IInventory p_i47233_2_) {
        super(new ContainerShulkerBox(p_i47233_1_, p_i47233_2_, Minecraft.getMinecraft().player));
        this.field_190780_w = p_i47233_1_;
        this.field_190779_v = p_i47233_2_;
        ++this.ySize;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (mc.player != null && mc.world != null) {
            float alpha = 160;
            int step = (int) (alpha / 100);
            if (this.curAlpha < alpha - step) {
                this.curAlpha += step;
            } else if (this.curAlpha > alpha - step && this.curAlpha != alpha) {
                this.curAlpha = (int) alpha;

            } else if (this.curAlpha != alpha) {
                this.curAlpha = (int) alpha;
            }
            Color c = new Color(Main.getClientColor().getRed(), Main.getClientColor().getGreen(), Main.getClientColor().getBlue(), (int) curAlpha);
            Color none = new Color(0, 0, 0, 0);
            this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), c.getRGB(), none.getRGB());
        } else
            this.drawDefaultBackground();
        //  this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), c.getRGB(), none.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.func_191948_b(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(this.field_190779_v.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.field_190780_w.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_190778_u);
        int i = (width - this.xSize) / 2;
        int j = (height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}
