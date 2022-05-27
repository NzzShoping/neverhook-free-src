package ru.neverhook.feature.player;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventMotion;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.other.MathUtils;
import ru.neverhook.utils.other.TimerUtils;

public class ChestStealer extends Feature {

    private final Setting delay;
    private final Setting drop;
    private final Setting noMove;
    TimerUtils timer = new TimerUtils();
    TimerUtils timerClose = new TimerUtils();

    public ChestStealer() {
        super("ChestStealer", Category.Player);
        Main.instance.setmgr.addSetting(this.delay = new Setting("Stealer Speed", this, 10, 0, 100, 1));
        Main.instance.setmgr.addSetting(this.drop = new Setting("Drop Items", this, false));
        Main.instance.setmgr.addSetting(this.noMove = new Setting("No Move Swap", this, false));
    }

    @EventTarget
    public void onChestStealer(EventMotion event) {
        if (event.isPre()) {
            this.setSuffix("" + Main.instance.setmgr.getSettingByName("Stealer Speed").getValInt());

            float delay = this.delay.getValFloat() * 10;

            if (mc.currentScreen instanceof GuiChest) {

                if (noMove.getValue() && mc.player.isMoving()) return;

                GuiChest chest = (GuiChest) mc.currentScreen;
                for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); index++) {
                    ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                    ContainerChest container = (ContainerChest) mc.player.openContainer;
                    if (isWhiteItem(stack))
                        if (this.timerClose.hasReached(delay)) {
                            if (!this.drop.getValue()) {
                                mc.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, mc.player);
                            } else {
                                mc.playerController.windowClick(container.windowId, index, 1, ClickType.THROW, mc.player);
                            }
                            this.timerClose.reset();
                        }
                }
                if (this.isEmpty(chest)) {
                    if (this.timer.hasReached(MathUtils.getRandomInRange(75, 150))) {
                        mc.player.closeScreen();
                    }
                } else {
                    this.timer.reset();
                }
            }
        }

        if (mc.currentScreen == null) {
            this.timer.reset();
        }
    }

    public boolean isWhiteItem(ItemStack itemStack) {
        return (itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemEnderPearl || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBlock || itemStack.getItem() instanceof ItemArrow || itemStack.getItem() instanceof ItemCompass);
    }

    private boolean isEmpty(GuiChest chest) {
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); index++) {
            ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (isWhiteItem(stack))
                return false;
        }
        return true;
    }
}
