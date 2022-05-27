package ru.neverhook.feature.movement;

import net.minecraft.init.MobEffects;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.movement.MovementUtil;

public class WaterSpeed extends Feature {

    public Setting waterSpeed;
    public Setting potionCheck;

    public WaterSpeed() {
        super("WaterSpeed", Category.Movement);
        Main.instance.setmgr.addSetting(waterSpeed = new Setting("Water Speed", this, 1, 0.1, 2, 0.01));
        Main.instance.setmgr.addSetting(potionCheck = new Setting("Speed Potion Check", this, false));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!mc.player.isPotionActive(MobEffects.SPEED) && potionCheck.getValue())
            return;
        if (mc.player.isInLiquid() && mc.player.isMoving()) {
            MovementUtil.setSpeed(waterSpeed.getValFloat());
        }
    }
}
