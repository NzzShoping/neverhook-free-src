package ru.neverhook.feature.visual;

import net.minecraft.client.renderer.GlStateManager;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventRenderScoreboard;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class ScoreBoard extends Feature {

    public Setting x;
    public Setting y;

    public ScoreBoard() {
        super("Scoreboard", Category.Visuals);
        Main.instance.setmgr.addSetting(new Setting("NoScoreBoard", this, false));
        Main.instance.setmgr.addSetting(this.x = new Setting("Scoreboard X", this, 0.0D, -1000.0D, 1000.0D, 1));
        Main.instance.setmgr.addSetting(this.y = new Setting("Scoreboard Y", this, 0.0D, -500.0D, 500.0D, 1));
    }

    @EventTarget
    public void onRenderScoreboard(EventRenderScoreboard event) {
        if (event.isPre()) {
            GlStateManager.translate(-this.x.getValDouble(), this.y.getValDouble(), 1.0D);
        } else {
            GlStateManager.translate(this.x.getValDouble(), -this.y.getValDouble(), 1.0D);
        }
    }
}
