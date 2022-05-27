package ru.neverhook.feature.visual;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.Event3D;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.visual.RenderUtil;

import java.awt.*;

public class Tracers extends Feature {

    public static Setting clientColor;
    public static Setting colorGlobal;
    public static Setting friend;

    public Tracers() {
        super("Tracers", Category.Visuals);
        Main.instance.setmgr.addSetting(colorGlobal = new Setting("Tracer Color", this, new Color(0xFFFFFF).getRGB(), true));
        Main.instance.setmgr.addSetting(clientColor = new Setting("ClientColors", this, false));
        Main.instance.setmgr.addSetting(friend = new Setting("Friend Highlight", this, true));
    }

    @EventTarget
    public void onEvent3D(Event3D event) {
        Color color = clientColor.getValue() ? Main.getClientColor() : Color.white;
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity != mc.player && entity != null) {
                if (!(entity instanceof EntityPlayer))
                    continue;
                RenderUtil.tracersEsp(entity, event.getPartialTicks(), color);
            }
        }
    }
}
