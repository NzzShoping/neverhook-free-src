package net.minecraft.client.gui;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import ru.neverhook.utils.other.ConnectionUtils;
import ru.neverhook.utils.visual.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiDisconnected extends GuiScreen {
    private final String reason;
    private final ITextComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int textHeight;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, ITextComponent chatComp) {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey);
        this.message = chatComp;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), width - 50);
        this.textHeight = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, Math.min(height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT, height - 30), I18n.format("gui.toMenu")));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, Math.min(height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT + 23, height - 30), I18n.format("Reconnect")));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(this.parentScreen);
                break;
            case 1:
                mc.loadWorld(null);
                mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, ConnectionUtils.serverData));
                break;
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);

        RenderUtil.drawBorderedRect(-5.0D, 0D, sr.getScaledWidth() - -6, sr.getScaledHeight(), 0.5D, (new Color(40, 40, 40, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
        RenderUtil.drawBorderedRect(1.0D, 1.4D, sr.getScaledWidth() - 1, sr.getScaledHeight() - 1.7, 0.5D, (new Color(22, 22, 22, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);

        this.drawCenteredString(this.fontRendererObj, this.reason, width / 2, height / 2 - this.textHeight / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = height / 2 - this.textHeight / 2;
        if (this.multilineMessage != null) {
            for (String s : this.multilineMessage) {
                this.drawCenteredString(this.fontRendererObj, s, width / 2, i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
