package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class GuiErrorScreen extends GuiScreen {
    private final String title;
    private final String message;

    public GuiErrorScreen(String titleIn, String messageIn) {
        this.title = titleIn;
        this.message = messageIn;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, width / 2 - 100, 140, I18n.format("gui.cancel")));
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, width, height, -12574688, -11530224);
        this.drawCenteredString(this.fontRendererObj, this.title, width / 2, 90, 16777215);
        this.drawCenteredString(this.fontRendererObj, this.message, width / 2, 110, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        this.mc.displayGuiScreen(null);
    }
}
