package ru.neverhook.feature.visual;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.Event2D;
import ru.neverhook.event.impl.Event3D;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.feature.world.StreamerMode;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.other.MathUtils;
import ru.neverhook.utils.visual.ColorUtils;
import ru.neverhook.utils.visual.RenderUtil;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NameTags extends Feature {

    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap();
    public Setting armor;
    public Setting opacity;

    public NameTags() {
        super("NameTags", Category.Visuals);
        Main.instance.setmgr.addSetting(new Setting("NameTags Size", this, 1, 0.5, 2, 0.1));
        Main.instance.setmgr.addSetting(new Setting("NameBackground", this, true));
        Main.instance.setmgr.addSetting(opacity = new Setting("Border Opacity", this, 10, 0, 255, 5));
        Main.instance.setmgr.addSetting(new Setting("OffHand Render", this, true));
        Main.instance.setmgr.addSetting(armor = new Setting("Show Armor", this, true));
    }

    public static TextFormatting getHealthColor(float health) {
        if (health <= 4)
            return TextFormatting.RED;
        else if (health <= 8)
            return TextFormatting.GOLD;
        else if (health <= 12)
            return TextFormatting.YELLOW;
        else if (health <= 16)
            return TextFormatting.DARK_GREEN;
        else
            return TextFormatting.GREEN;
    }

    @EventTarget
    public void on3D(Event3D event) {
        try {
            updatePositions();
        } catch (Exception ignored) {
        }
    }

    @EventTarget
    public void on2D(Event2D event) {
        GlStateManager.pushMatrix();
        for (EntityLivingBase entity : entityPositions.keySet()) {
            GlStateManager.pushMatrix();
            if (entity instanceof EntityPlayer && entity != mc.player) {
                double[] array = entityPositions.get(entity);
                if (array[3] < 0.0 || array[3] >= 1.0) {
                    GlStateManager.popMatrix();
                    continue;
                }
                GlStateManager.translate(array[0] / ScaledResolution.getScaleFactor(), array[1] / ScaledResolution.getScaleFactor(), 0.0);
                this.scale();
                String string = "";
                if (Main.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.nameSpoof.getValue()) {
                    string = "Protected";
                } else if (Main.instance.friendManager.isFriend(entity.getName())) {
                    string = ChatFormatting.GREEN + "[F] " + ChatFormatting.RESET + entity.getDisplayName().getUnformattedText();
                } else {
                    string = entity.getDisplayName().getUnformattedText();
                }
                String string2 = "" + MathUtils.round(entity.getHealth(), 1);
                float width = mc.fontRendererObj.getStringWidth(string2 + " " + string) + 2;
                GlStateManager.translate(0.0D, -10, 0.0D);
                if (Main.instance.setmgr.getSettingByName("NameBackground").getValue()) {
                    RenderUtil.drawBorderedRect((-width / 2), -10.0D, (width / 2), 3, 1, ColorUtils.getColor(0, opacity.getValInt()), ColorUtils.getColor(25, opacity.getValInt()), true);
                }
                mc.fontRendererObj.drawStringWithShadow(string + " " + getHealthColor(entity.getHealth()) + string2, (-width / 2 + 2), -7.5f, -1);
                ItemStack heldItemStack = entity.getHeldItem(EnumHand.OFF_HAND);
                if (armor.getValue()) {
                    ArrayList<ItemStack> list = new ArrayList<>();
                    for (int i = 0; i < 5; ++i) {
                        ItemStack getEquipmentInSlot = ((EntityPlayer) entity).getEquipmentInSlot(i);
                        list.add(getEquipmentInSlot);
                    }
                    int n10 = -(list.size() * 9);
                    if (Main.instance.setmgr.getSettingByName("OffHand Render").getValue()) {
                        mc.getRenderItem().renderItemIntoGUI(heldItemStack, n10 + 105 - (mc.fontRendererObj.getStringWidth("" + n10)), -29);
                        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, heldItemStack, n10 + 105 - (mc.fontRendererObj.getStringWidth("" + n10)), -28);
                    }
                    for (ItemStack itemStack : list) {
                        RenderHelper.enableGUIStandardItemLighting();
                        mc.getRenderItem().renderItemIntoGUI(itemStack, n10 + 6, -28);
                        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemStack, n10 + 5, -26);
                        n10 += 3;
                        RenderHelper.disableStandardItemLighting();
                        int n11 = 7;
                        int getEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(16)), itemStack);
                        int getEnchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(20)), itemStack);
                        int getEnchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(19)), itemStack);
                        if (getEnchantmentLevel > 0) {
                            this.drawEnchantTag("S" + this.getColor(getEnchantmentLevel) + getEnchantmentLevel, n10, n11);
                            n11 += 8;
                        }
                        if (getEnchantmentLevel2 > 0) {
                            this.drawEnchantTag("F" + this.getColor(getEnchantmentLevel2) + getEnchantmentLevel2, n10, n11);
                            n11 += 8;
                        }
                        if (getEnchantmentLevel3 > 0) {
                            this.drawEnchantTag("Kb" + this.getColor(getEnchantmentLevel3) + getEnchantmentLevel3, n10, n11);
                        } else if (itemStack.getItem() instanceof ItemArmor) {
                            int getEnchantmentLevel4 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(0)), itemStack);
                            int getEnchantmentLevel5 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(7)), itemStack);
                            int getEnchantmentLevel6 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(34)), itemStack);
                            if (getEnchantmentLevel4 > 0) {
                                this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel4) + getEnchantmentLevel4, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel5 > 0) {
                                this.drawEnchantTag("Th" + this.getColor(getEnchantmentLevel5) + getEnchantmentLevel5, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel6 > 0) {
                                this.drawEnchantTag("U" + this.getColor(getEnchantmentLevel6) + getEnchantmentLevel6, n10, n11);
                            }
                        } else if (itemStack.getItem() instanceof ItemBow) {
                            int getEnchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(48)), itemStack);
                            int getEnchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(49)), itemStack);
                            int getEnchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(50)), itemStack);
                            if (getEnchantmentLevel7 > 0) {
                                this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel7) + getEnchantmentLevel7, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel8 > 0) {
                                this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel8) + getEnchantmentLevel8, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel9 > 0) {
                                this.drawEnchantTag("F" + this.getColor(getEnchantmentLevel9) + getEnchantmentLevel9, n10, n11);
                            }
                        }
                        int n12 = (int) Math.round(255.0 - itemStack.getItemDamage() * 255.0 / itemStack.getMaxDamage());
                        new Color(255 - n12 << 16 | n12 << 8).brighter();
                        if (itemStack.getMaxDamage() - itemStack.getItemDamage() > 0) {
                            GlStateManager.pushMatrix();
                            GlStateManager.disableDepth();
                            GlStateManager.enableDepth();
                            GlStateManager.popMatrix();
                        }
                        n10 += 13.5;
                    }
                }
                GlStateManager.popMatrix();
            }
        }
        GL11.glPopMatrix();
        GlStateManager.enableBlend();
    }

    private void drawEnchantTag(String text, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        n2 -= 7;
        mc.fontRendererObj.drawStringWithShadow(text, n + 6, -35 - n2, -1);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private String getColor(int n) {
        if (n != 1) {
            if (n == 2) {
                return "";
            }
            if (n == 3) {
                return "";
            }
            if (n == 4) {
                return "";
            }
            if (n >= 5) {
                return "";
            }
        }
        return "";
    }

    private void updatePositions() {
        entityPositions.clear();
        float pTicks = mc.timer.renderPartialTicks;
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityPlayer && entity != mc.player) {
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * pTicks - mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * pTicks - mc.getRenderManager().renderPosY;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * pTicks - mc.getRenderManager().renderPosZ;
                y += entity.height + 0.2D;
                if ((Objects.requireNonNull(convertTo2D(x, y, z))[2] >= 0.0D) && (Objects.requireNonNull(convertTo2D(x, y, z))[2] < 1.0D)) {
                    entityPositions.put((EntityPlayer) entity, new double[]{Objects.requireNonNull(convertTo2D(x, y, z))[0], Objects.requireNonNull(convertTo2D(x, y, z))[1], Math.abs(convertTo2D(x, y + 1.0D, z, entity)[1] - convertTo2D(x, y, z, entity)[1]), convertTo2D(x, y, z)[2]});
                }
            }
        }
    }

    private double[] convertTo2D(double x, double y, double z, Entity ent) {
        float pTicks = mc.timer.renderPartialTicks;
        mc.entityRenderer.setupCameraTransform(pTicks, 0);
        double[] convertedPoints = convertTo2D(x, y, z);
        mc.entityRenderer.setupCameraTransform(pTicks, 0);
        return convertedPoints;
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCords);
        if (result) {
            return new double[]{screenCords.get(0), Display.getHeight() - screenCords.get(1), screenCords.get(2)};
        }
        return null;
    }

    private void scale() {
        float n = (mc.gameSettings.smoothCamera ? 2.0f : Main.instance.setmgr.getSettingByName("NameTags Size").getValFloat());
        GlStateManager.scale(n, n, n);
    }
}