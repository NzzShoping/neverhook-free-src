package ru.neverhook.feature.world;

import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventNameTag;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class StreamerMode extends Feature {

    public static Setting tabSpoof;
    public static Setting nameSpoof;
    public static Setting skinSpoof;

    public StreamerMode() {
        super("StreamerMode", Category.World);
        Main.instance.setmgr.addSetting(nameSpoof = new Setting("Name Spoof", this, true));
        Main.instance.setmgr.addSetting(skinSpoof = new Setting("Skin Spoof", this, true));
        Main.instance.setmgr.addSetting(tabSpoof = new Setting("Tab Spoof", this, true));
    }

    @EventTarget
    public void onNameTag(EventNameTag event) {
        if (nameSpoof.getValue()) {
            event.setRenderedName("Protected");
        }
    }
}
