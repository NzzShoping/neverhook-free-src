package ru.neverhook.feature.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.Event2D;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.awt.*;

public class HurtCam extends Feature {

    private final Setting gradient;
    private final Setting gradientColor;
    private final Setting gradientAlpha;
    private float curAlpha;

    public HurtCam() {
        super("HurtCam", Category.Visuals);
        Main.instance.setmgr.addSetting(this.gradient = new Setting("Gradient Hurt", this, true));
        Main.instance.setmgr.addSetting(this.gradientAlpha = new Setting("Gradient Alpha", this, 70, 10, 255, 10));
        Main.instance.setmgr.addSetting(this.gradientColor = new Setting("Gradient Color", this, new Color(0xFFFFFF).getRGB(), true));
    }

    @EventTarget
    public void onRender2D(Event2D event) {
        if (mc.player == null && mc.world == null)
            return;
        if (gradient.getValue()) {
            if (mc.player.hurtTime > 0) {
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                float alpha = gradientAlpha.getValFloat();
                int step = (int) (alpha / 100);
                if (this.curAlpha < alpha - step) {
                    this.curAlpha += step;
                } else if (this.curAlpha > alpha - step && this.curAlpha != alpha) {
                    this.curAlpha = (int) alpha;

                } else if (this.curAlpha != alpha) {
                    this.curAlpha = (int) alpha;
                }
                Color onecolor = new Color(gradientColor.getColorValue());
                Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), (int) alpha);
                Color none = new Color(0, 0, 0, 0);
                Gui gui = new Gui();
                gui.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() - 505, c.getRGB(), none.getRGB());
                gui.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() + 255, none.getRGB(), c.getRGB());
            }
        }
    }
}
