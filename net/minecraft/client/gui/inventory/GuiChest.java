package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import ru.neverhook.Main;

import java.awt.*;
import java.io.IOException;

public class GuiChest extends GuiContainer {
    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private final IInventory upperChestInventory;
    public final IInventory lowerChestInventory;

    /**
     * window height is calculated with these values; the more rows, the heigher
     */
    private final int inventoryRows;
    private float curAlpha;

    public GuiChest(IInventory upperInv, IInventory lowerInv) {
        super(new ContainerChest(upperInv, lowerInv, Minecraft.getMinecraft().player));
        this.upperChestInventory = upperInv;
        this.lowerChestInventory = lowerInv;
        this.allowUserInput = false;
        int i = 222;
        int j = 114;
        this.inventoryRows = lowerInv.getSizeInventory() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (mc.player != null && mc.world != null) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
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

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.func_191948_b(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */

    @Override
    public void initGui() {
        int posY = (height - ySize) / 2 + 5;
        buttonList.add(new GuiButton(1, width / 2 - 5, posY, 40, 10, "Steal"));
        buttonList.add(new GuiButton(2, width / 2 + 40, posY, 40, 10, "Store"));
        super.initGui();
    }

    public IInventory getLowerChestInventory() {
        return lowerChestInventory;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 1) {
            new Thread(() -> {
                try {
                    for (int i = 0; i < GuiChest.this.inventoryRows * 9; i++) {
                        ContainerChest container = (ContainerChest) mc.player.openContainer;
                        if (container != null) {
                            Thread.sleep(50L);
                            mc.playerController.windowClick(container.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
                        }
                    }
                } catch (Exception e) {
                }

            }).start();
        } else if (button.id == 2) {
            new Thread(() -> {
                try {
                    for (int i = GuiChest.this.inventoryRows * 9; i < GuiChest.this.inventoryRows * 9 + 44; i++) {
                        Slot slot = GuiChest.this.inventorySlots.inventorySlots.get(i);
                        if (slot.getStack() != null) {
                            Thread.sleep(5L);
                            GuiChest.this.handleMouseClick(slot, slot.slotNumber, 0, ClickType.QUICK_MOVE);
                        }
                    }
                } catch (Exception e) {
                }

            }).start();
        }
    }

    public int getInventoryRows() {
        return inventoryRows;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawString(mc.fontRendererObj, this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, -1);
        drawString(mc.fontRendererObj, this.upperChestInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, -1);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (width - this.xSize) / 2;
        int j = (height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}