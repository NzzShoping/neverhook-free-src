package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import ru.neverhook.Main;
import ru.neverhook.feature.world.StreamerMode;
import ru.neverhook.ui.buttons.GuiMainMenuButton;
import ru.neverhook.ui.buttons.ImageButton;
import ru.neverhook.ui.changelog.ChangeLog;
import ru.neverhook.ui.clickgui.util.ParticleEngine;
import ru.neverhook.ui.login.GLSLSandboxShader;
import ru.neverhook.ui.newaltmanager.GuiAltManager;
import ru.neverhook.utils.font.FontRenderer;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiMainMenu extends GuiScreen {
    private int width;
    private int height;
    private GLSLSandboxShader backgroundShader;
    private final long initTime = System.currentTimeMillis();
    protected ArrayList<ImageButton> imageButtons = new ArrayList();
    public static ParticleEngine engine = new ParticleEngine();

    public GuiMainMenu() {
        try {
            this.backgroundShader = new GLSLSandboxShader("/noise.fsh");
        } catch (IOException e) {
        }
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.width = sr.getScaledWidth();
        this.height = sr.getScaledHeight();

        this.buttonList.add(new GuiMainMenuButton(0, (this.width / 2) - 90, this.height / 2 + 4, 180, 15, "Singleplayer"));
        this.buttonList.add(new GuiMainMenuButton(1, this.width / 2 - 90, this.height / 2 + 32, 180, 15, "Multiplayer"));
        this.buttonList.add(new GuiMainMenuButton(2, this.width / 2 - 90, this.height / 2 + 60, 180, 15, "Alt Manager"));
        this.imageButtons.clear();

        /* LANG */

        this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/world.png"), this.width / 2 + 100, this.height / 2 + 4, 24, 24, "Language", 15));

        /* OPTIONS */

        this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/misc.png"), this.width / 2 + 100, this.height / 2 + 34, 24, 24, "Options", 16));

        /* QUIT */

        this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/quit.png"), this.width / 2 + 105, this.height / 2 + 68, 15, 15, "Quit Game", 14));

        /* LOGO */

        this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/logo.png"), this.width / 2 - 30, this.height / 2 - 140, 60, 70, "", 13));

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution res = new ScaledResolution(mc);
        int width = res.getScaledWidth() / 2;
        int height = res.getScaledHeight() / 2;

        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.disableCull();

        this.backgroundShader.useShader(width, height * 4 + 150, mouseX, mouseY, (float) (System.currentTimeMillis() - this.initTime) / 200);

        GL11.glBegin(7);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);
        ScaledResolution sr = new ScaledResolution(mc);

        int y = 22;
        int x = 8;

        mc.fontRendererObj.drawStringWithShadow("ChangeLog", 3, 7, new Color(175, 175, 175, 255).getRGB());

        for (ChangeLog log : Main.getLogs().getLogs()) {
            if (log != null) {
                mc.fontRendererObj.drawStringWithShadow(log.getLogName(), x - 3, y - 3, -1);
                y += 10;
            }
        }
        mc.robotoLight.drawStringWithShadow("Made with love by Smertnix & MyLifeIsShit & Basic", 1, sr.getScaledHeight() - mc.robotoLight.getHeight() - 2, -1);
        mc.robotoLight.drawStringWithShadow("NeverHook Client " + "(#" + Main.build + ")", 10, sr.getScaledHeight() - mc.robotoLight.getHeight() - 14, -1);
        String welcome = "Welcome, " + mc.getSession().getUsername();
        mc.robotoLight.drawStringWithShadow((Main.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() ? "Welcome, Protected" : welcome), sr.getScaledWidth() - 2 - (mc.robotoLight.getStringWidth(Main.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() ? "Welcome, Protected" : welcome)), sr.getScaledHeight() - mc.robotoLight.getHeight() - 2, -1);
        FontRenderer.drawOutlinedStringWithMCFont("От разработчиков WintWare", mc.fontRendererObj, sr.getScaledWidth() - 2 - (mc.fontRendererObj.getStringWidth("От разработчиков WintWare")), 2, -1);

        for (ImageButton imageButton : this.imageButtons) {

            imageButton.draw(mouseX, mouseY, Color.WHITE);

            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }

        engine.render(mouseX, mouseY);

        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiWorldSelection(this));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiAltManager());
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 4:
                System.exit(0);
                break;
        }

        super.actionPerformed(button);
    }
}