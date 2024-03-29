package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;

public class GuiDownloadTerrain extends GuiScreen {
    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        this.buttonList.clear();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingTerrain"), width / 2, height / 2 - 50, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }
}
