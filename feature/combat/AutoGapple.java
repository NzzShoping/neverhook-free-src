package ru.neverhook.feature.combat;

import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class AutoGapple extends Feature {

    public boolean isActive;
    Setting health;

    public AutoGapple() {
        super("AutoGApple", Category.Combat);
        Main.instance.setmgr.addSetting(this.health = new Setting("Health Amount", this, 15, 1, 20, 1));
    }

    public static boolean isNullOrEmptyStack(ItemStack itemStack) {
        return (itemStack == null || itemStack.func_190926_b());
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        this.setSuffix("" + health.getValInt());

        if (isGApple(mc.player.getHeldItemOffhand()) && mc.player.getHealth() <= health.getValInt()) {
            isActive = true;
            mc.gameSettings.keyBindUseItem.pressed = true;
        } else if (isActive) {
            mc.gameSettings.keyBindUseItem.pressed = false;
            isActive = false;
        }
    }

    private boolean isGApple(ItemStack itemStack) {
        return (!isNullOrEmptyStack(itemStack) && itemStack.getItem() instanceof ItemAppleGold);
    }
}
