package ru.neverhook.feature.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventTransformSideFirstPerson;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class ViewModel extends Feature {

    public static Setting rightx;
    public static Setting righty;
    public static Setting rightz;
    public static Setting leftx;
    public static Setting lefty;
    public static Setting leftz;

    public ViewModel() {
        super("ViewModel", Category.Visuals);
        rightx = new Setting("RightX", this, 0, -2, 2, 0.1F);
        righty = new Setting("RightY", this, 0.2F, -2, 2, 0.1F);
        rightz = new Setting("RightZ", this, 0.2F, -2, 2, 0.1F);
        leftx = new Setting("LeftX", this, 0, -2, 2, 0.1F);
        lefty = new Setting("LeftY", this, 0.2F, -2, 2, 0.1F);
        leftz = new Setting("LeftZ", this, 0.2F, -2, 2, 0.1F);
        addSettings(rightx, righty, rightz, leftx, lefty, leftz);
    }

    @EventTarget
    public void onSidePerson(EventTransformSideFirstPerson event) {
        if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(rightx.getValDouble(), righty.getValDouble(), rightz.getValDouble());
        }
        if (event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(-leftx.getValDouble(), lefty.getValDouble(), leftz.getValDouble());
        }
    }

}
