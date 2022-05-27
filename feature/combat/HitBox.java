package ru.neverhook.feature.combat;

import net.minecraft.entity.Entity;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.other.MathUtils;

public class HitBox extends Feature {

    public static Setting expand;

    public HitBox() {
        super("HitBox", Category.Combat);
        Main.instance.setmgr.addSetting(expand = new Setting("Expand", this, 0.2D, 0.01D, 2.0D, 0.01));
    }

    public static float hitboxExpand(Entity entity) {
        if (entity.equals(mc.player) || !Main.instance.featureManager.getFeatureByClass(HitBox.class).getState()) {
            return 0.0F;
        }
        return expand.getValFloat();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathUtils.round(expand.getValFloat(), 1));
    }
}