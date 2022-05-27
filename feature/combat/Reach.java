package ru.neverhook.feature.combat;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.other.MathUtils;

public class Reach extends Feature {

    public static Setting expand;

    public Reach() {
        super("Reach", Category.Combat);
        Main.instance.setmgr.addSetting(expand = new Setting("Reach Expand", this, 3.2, 3, 5, 0.01));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathUtils.round(expand.getValFloat(), 1));
    }
}
