package ru.neverhook.feature.combat;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.other.TimerUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AutoArmor extends Feature {

    private final Setting delay;
    TimerUtils timerUtils = new TimerUtils();

    public AutoArmor() {
        super("AutoArmor", Category.Combat);
        Main.instance.setmgr.addSetting(this.delay = new Setting("Equip Delay", this, 1, 0, 10, 1));
    }

    public static boolean isNullOrEmpty(ItemStack stack) {
        return !(stack != null && !stack.func_190926_b());
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        this.setSuffix("" + delay.getValFloat());

        if (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof InventoryEffectRenderer)) {
            return;
        }

        InventoryPlayer inventory = mc.player.inventory;

        int[] bestArmorSlots = new int[4];
        int[] bestArmorValues = new int[4];


        for (int type = 0; type < 4; type++) {

            bestArmorSlots[type] = -1;

            ItemStack stack = inventory.armorItemInSlot(type);
            if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {

                ItemArmor item = (ItemArmor) stack.getItem();
                bestArmorValues[type] = getArmorValue(item, stack);
            }
        }

        for (int slot = 0; slot < 36; slot++) {

            ItemStack stack = inventory.getStackInSlot(slot);

            if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {

                ItemArmor item = (ItemArmor) stack.getItem();
                int armorType = item.armorType.getIndex();
                int armorValue = getArmorValue(item, stack);

                if (armorValue > bestArmorValues[armorType]) {

                    bestArmorSlots[armorType] = slot;
                    bestArmorValues[armorType] = armorValue;
                }
            }
        }

        ArrayList<Integer> types = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(types);

        for (int i : types) {
            int j = bestArmorSlots[i];
            if (j == -1) {
                continue;
            }
            ItemStack oldArmor = inventory.armorItemInSlot(i);
            if (!isNullOrEmpty(oldArmor) &&
                    inventory.getFirstEmptyStack() == -1) {
                continue;
            }
            if (j < 9) {
                j += 36;
            }
            if (timerUtils.hasReached(delay.getValFloat() * 100)) {
                if (!isNullOrEmpty(oldArmor)) {
                    mc.playerController.windowClick(0, 8 - i, 0, ClickType.QUICK_MOVE, mc.player);
                }
                mc.playerController.windowClick(0, j, 0, ClickType.QUICK_MOVE, mc.player);
                timerUtils.reset();
            }
            break;
        }
    }

    private int getArmorValue(ItemArmor item, ItemStack stack) {
        int armorPoints = item.damageReduceAmount;
        int prtPoints = 0;
        int armorToughness = (int) item.toughness;
        int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
        Enchantment protection = Enchantments.PROTECTION;
        int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
        DamageSource dmgSource = DamageSource.causePlayerDamage(mc.player);
        prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }
}
