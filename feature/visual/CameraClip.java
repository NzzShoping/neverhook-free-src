package ru.neverhook.feature.visual;

import ru.neverhook.Main;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class CameraClip extends Feature {

    public static Setting fovModifier;

    public CameraClip() {
        super("CameraNoClip", Category.Visuals);
        Main.instance.setmgr.addSetting(fovModifier = new Setting("Camera FOV", this, 4, 1, 50, 1));
    }
}
