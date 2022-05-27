package ru.neverhook.feature.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.Event2D;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.inventory.InventoryUtil;
import ru.neverhook.utils.visual.RenderUtil;

public class AutoTotem extends Feature {

    public Setting health;
    public Setting counttotem;
    public Setting checkcrystal;
    public Setting radius;

    public AutoTotem() {
        super("AutoTotem", Category.Combat);
        Main.instance.setmgr.addSetting(health = new Setting("Health", this, 10, 1, 20, 1));
        Main.instance.setmgr.addSetting(counttotem = new Setting("Count Totem", this, true));
        Main.instance.setmgr.addSetting(checkcrystal = new Setting("Check Crystal", this, true));
        Main.instance.setmgr.addSetting(radius = new Setting("Distance to Crystal", this, 6, 1, 8, 1));
    }

    @EventTarget
    public void on2D(Event2D event) {
        if (fountTotemCount() > 0 && counttotem.getValue()) {
            mc.arraylist.drawStringWithShadow(fountTotemCount() + "", (event.getResolution().getScaledWidth() / 2f + 19), (event.getResolution().getScaledHeight() / 2f), -1);
            for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                    RenderUtil.renderItem(stack, event.getResolution().getScaledWidth() / 2 + 4, event.getResolution().getScaledHeight() / 2 - 7);
                }
            }
        }
    }

    public int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                count++;
            }
        }
        return count;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && InventoryUtil.getTotemAtHotbar() != -1 && mc.player.getHealth() <= this.health.getValDouble()) {
            mc.playerController.windowClick(0, InventoryUtil.getTotemAtHotbar(), 1, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, mc.player);
        }
        if (checkCrystal() && checkcrystal.getValue()) {
            if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && InventoryUtil.getTotemAtHotbar() != -1) {
                mc.playerController.windowClick(0, InventoryUtil.getTotemAtHotbar(), 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, mc.player);
            }
        }
    }

    private boolean checkCrystal() {
        for (Entity entity : mc.world.loadedEntityList) {
            if ((entity instanceof EntityEnderCrystal && mc.player.getDistanceToEntity(entity) <= radius.getValFloat())) {
                return true;
            }
        }
        return false;
    }
}
