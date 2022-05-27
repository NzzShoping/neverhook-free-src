package ru.neverhook.feature.visual;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.Event2D;
import ru.neverhook.event.impl.Event3D;
import ru.neverhook.event.impl.EventRenderName;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.feature.world.StreamerMode;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.font.FontRenderer;
import ru.neverhook.utils.visual.RenderUtil;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class ESP extends Feature {

    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
    private final int backgroundColor = (new Color(0, 0, 0, 120)).getRGB();
    private final int black = Color.BLACK.getRGB();

    private final Setting rect;
    private final Setting armor;
    private final Setting client;
    private final Setting tags;

    private final Setting colorEsp;
    private final Setting itemEsp;
    private final Setting espItems;

    public ESP() {
        super("ESP", Category.Visuals);
        ArrayList<String> options = new ArrayList<>();
        options.add("CSGO");
        options.add("None");
        Main.instance.setmgr.addSetting(new Setting("ESP Mode", this, "CSGO", options));
        Main.instance.setmgr.addSetting(rect = new Setting("Health Rect", this, true));
        Main.instance.setmgr.addSetting(armor = new Setting("Render Armor", this, true));
        Main.instance.setmgr.addSetting(tags = new Setting("Render Tags", this, true));
        Main.instance.setmgr.addSetting(espItems = new Setting("Item ESP", this, false));
        Main.instance.setmgr.addSetting(colorEsp = new Setting("ESP Color", this, new Color(0xFFFFFF).getRGB(), true));
        Main.instance.setmgr.addSetting(itemEsp = new Setting("Item ESP Color", this, new Color(0xFFFFFF).getRGB(), true));
        Main.instance.setmgr.addSetting(client = new Setting("Client Color", this, false));
    }

    @EventTarget
    public void onRender3D(Event3D event3D) {
        if (!this.getState())
            return;

        Color onecolor = new Color(itemEsp.getColorValue());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        if (espItems.getValue()) {
            for (Entity item : mc.world.loadedEntityList) {
                if (item instanceof EntityItem) {
                    if (!client.getValue()) {
                        GlStateManager.color(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, 0.3F);
                        RenderUtil.entityESPBox(item, c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, c.getAlpha() / 255F);
                    } else {
                        RenderUtil.entityESPBox(item, Main.getClientColor().getRed() / 255F, Main.getClientColor().getGreen() / 255F, Main.getClientColor().getBlue() / 255F, Main.getClientColor().getAlpha() / 255F);
                        GlStateManager.color(Main.getClientColor().getRed() / 255F, Main.getClientColor().getGreen() / 255F, Main.getClientColor().getBlue() / 255F, 0.3F);
                    }
                }
            }
        }
    }

    @EventTarget
    public void onRender2D(Event2D event) {
        String mode = Main.instance.setmgr.getSettingByName("ESP Mode").getValString();
        this.setSuffix(mode);
        GL11.glPushMatrix();
        float partialTicks = mc.timer.renderPartialTicks;
        int scaleFactor = ScaledResolution.getScaleFactor();
        double scaling = scaleFactor / Math.pow(scaleFactor, 2.0D);
        GL11.glScaled(scaling, scaling, scaling);
        int black = this.black;
        Color onecolor = new Color(colorEsp.getColorValue());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = client.getValue() ? Main.getClientColor().getRGB() : c.getRGB();
        float scale = 1F;
        float upscale = 1.0F / scale;
        RenderManager renderMng = mc.getRenderManager();
        EntityRenderer entityRenderer = mc.entityRenderer;

        for (Entity entity : mc.world.loadedEntityList) {
            if (isValid(entity) && RenderUtil.isInViewFrustrum(entity)) {
                double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
                double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
                double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
                double width = entity.width / 1.5D;
                double height = entity.height + ((entity.isSneaking() || (entity == mc.player && mc.player.isSneaking()) ? -0.3D : 0.2D));
                AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                Vector3d[] vectors = new Vector3d[]{new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ),
                        new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
                entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
                    if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
                        if (position == null)
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0D);
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                if (position != null) {
                    entityRenderer.setupOverlayRendering();
                    double posX = position.x;
                    double posY = position.y;
                    double endPosX = position.z;
                    double endPosY = position.w;
                    if (mode.equalsIgnoreCase("CSGO")) {
                        RenderUtil.drawRect(posX + 1D, posY, posX - 1.0D, posY + (endPosY - posY) / 4.0D + 0.5D, black);
                        RenderUtil.drawRect(posX - 2.0D, endPosY, posX + 0.5D, endPosY - (endPosY - posY) / 4.0D - 0.5D, black);
                        RenderUtil.drawRect(posX - 2.0D, posY - 0.5D, posX + (endPosX - posX) / 3.0D + 0.5D, posY + 1.0D, black);
                        RenderUtil.drawRect(endPosX - (endPosX - posX) / 3.0D - 1D, posY - 0.5D, endPosX, posY + 1.0D, black);
                        RenderUtil.drawRect(endPosX - 2.0D, posY, endPosX + 0.5D, posY + (endPosY - posY) / 4.0D + 0.5D, black);
                        RenderUtil.drawRect(endPosX - 2.0D, endPosY, endPosX + 0.5D, endPosY - (endPosY - posY) / 4.0D - 0.5D, black);
                        RenderUtil.drawRect(posX - 2.0D, endPosY - 1.0D, posX + (endPosX - posX) / 3.0D + 0.5D, endPosY + 0.5D, black);
                        RenderUtil.drawRect(endPosX - (endPosX - posX) / 3.0D - 1D, endPosY - 1.0D, endPosX + 0.5D, endPosY + 0.5D, black);
                        RenderUtil.drawRect(posX, posY, posX - 1D, posY + (endPosY - posY) / 4.0D, color);
                        RenderUtil.drawRect(posX - 1, endPosY, posX, endPosY - (endPosY - posY) / 4.0D, color);
                        RenderUtil.drawRect(posX - 1D, posY, posX + (endPosX - posX) / 3.0D, posY + 0.5D, color);
                        RenderUtil.drawRect(endPosX - (endPosX - posX) / 3.0D - 1, posY, endPosX, posY + 0.5D, color);
                        RenderUtil.drawRect(endPosX - 1D, posY, endPosX, posY + (endPosY - posY) / 4.0D, color);
                        RenderUtil.drawRect(endPosX - 1D, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0D, color);
                        RenderUtil.drawRect(posX - 1, endPosY - 0.5, posX + (endPosX - posX) / 3.0D, endPosY, color);
                        RenderUtil.drawRect(endPosX - (endPosX - posX) / 3.0D - 2, endPosY - 0.5D, endPosX - 0.5D, endPosY, color);
                    }
                    boolean living = entity instanceof EntityLivingBase;

                    int healthColor2;
                    EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                    float hp2 = entityLivingBase.getHealth();
                    float maxHealth = entityLivingBase.getMaxHealth();
                    double hpPercentage = (hp2 / maxHealth);
                    double hpHeight2 = (endPosY - posY) * hpPercentage;
                    if (hp2 <= 4) {
                        healthColor2 = new Color(200, 0, 0).getRGB();
                    } else if (hp2 <= 8) {
                        healthColor2 = new Color(231, 143, 85).getRGB();
                    } else if (hp2 <= 12) {
                        healthColor2 = new Color(219, 201, 106).getRGB();
                    } else if (hp2 <= 16) {
                        healthColor2 = new Color(117, 231, 85).getRGB();
                    } else {
                        healthColor2 = new Color(44, 186, 19).getRGB();
                    }

                    if (entityLivingBase != null && hp2 > 0) {

                        if (living && rect.getValue()) {
                            MathHelper.clamp(hp2, 0, 20);
                            RenderUtil.drawRect(posX - 4.5F, posY - 0.7F, posX - 1F, endPosY + 0.3F, new Color(0, 0, 0, 255).getRGB());
                            if (hp2 > 0) {
                                if (mc.player.getDistanceToEntity(entity) <= 8) {
                                    RenderUtil.drawRect(posX - 3.5F, endPosY, posX - 2.0F, endPosY - hpHeight2 - (((EntityLivingBase) entity).getHealth() < 18 ? 5 : 0), new Color(255, 0, 0).getRGB());
                                }
                                RenderUtil.drawRect(posX - 3.5F, endPosY, posX - 2.0F, endPosY - hpHeight2, healthColor2);
                            }
                        }
                        if (living && tags.getValue()) {
                            if (!(Main.instance.featureManager.getFeatureByClass(NameTags.class).getState())) {
                                float scaledHeight = 20.0F;
                                String name = entity.getName();
                                if (Main.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.nameSpoof.getValue()) {
                                    name = "Protected";
                                }
                                double dif = (endPosX - posX) / 2.0D;
                                double textWidth = (mc.fontRenderer.getStringWidth(name + " §7" + (int) mc.player.getDistanceToEntity(entity) + "m") * scale);
                                float tagX = (float) ((posX + dif - textWidth / 2.0D) * upscale);
                                float tagY = (float) (posY * upscale) - scaledHeight;
                                GL11.glPushMatrix();
                                GlStateManager.scale(scale, scale, scale);
                                FontRenderer.drawOutlinedStringWithMCFont(name, mc.fontRendererObj, tagX + 5, tagY + 5, -1);
                                GL11.glPopMatrix();
                            }
                        }

                        if (living && armor.getValue()) {
                            if (entity instanceof EntityPlayer) {
                                EntityPlayer player = (EntityPlayer) entity;
                                double ydiff = (endPosY - posY) / 4;

                                ItemStack stack = (player).getEquipmentInSlot(4);
                                if (mc.player.getDistanceToEntity(player) <= 15) {
                                    if (stack != null) {
                                        double diff1 = (posY + ydiff - 1) - (posY + 2);
                                        double percent = 1 - (double) stack.getItemDamage() / (double) stack.getMaxDamage();
                                        RenderUtil.renderItem(stack, (int) endPosX + 4, (int) posY + (int) ydiff - 1 - (int) (diff1 / 2) - 18);
                                        //mc.smallfontRenderer.drawStringWithShadow(stackname, (float) endPosX + 5, (float) (posY + ydiff - 1 - (diff1 / 2)) - (mc.clickguismall.getStringHeight(stack.getMaxDamage() - stack.getItemDamage() + "") / 2), color);
                                    }
                                    ItemStack stack2 = (player).getEquipmentInSlot(3);
                                    if (stack2 != null) {
                                        double diff1 = (posY + ydiff * 2) - (posY + ydiff + 2);
                                        String stackname = (stack.getDisplayName().equalsIgnoreCase("Air")) ? "0" : !(stack2.getItem() instanceof ItemArmor) ? stack2.getDisplayName() : stack2.getMaxDamage() - stack2.getItemDamage() + "";
                                        if (mc.player.getDistanceToEntity(player) < 10) {
                                            RenderUtil.renderItem(stack2, (int) endPosX + 4, (int) (posY + ydiff * 2) - (int) (diff1 / 2) - 18);
                                            //mc.smallfontRenderer.drawStringWithShadow(stackname, (float) endPosX + 5, (float) ((posY + ydiff * 2) - (diff1 / 2)) - (mc.clickguismall.getStringHeight(stack2.getMaxDamage() - stack2.getItemDamage() + "") / 2), color);
                                        }
                                    }
                                    ItemStack stack3 = (player).getEquipmentInSlot(2);
                                    if (stack3 != null) {

                                        double diff1 = (posY + ydiff * 3) - (posY + ydiff * 2 + 2);
                                        if (mc.player.getDistanceToEntity(player) < 10) {
                                            RenderUtil.renderItem(stack3, (int) endPosX + 4, (int) (posY + ydiff * 3) - (int) (diff1 / 2) - 18);
                                            //mc.smallfontRenderer.drawStringWithShadow(stackname, (float) endPosX + 5, (float) ((posY + ydiff * 3) - (diff1 / 2)) - (mc.clickguismall.getStringHeight(stack3.getMaxDamage() - stack3.getItemDamage() + "") / 2), color);
                                        }
                                    }
                                    ItemStack stack4 = (player).getEquipmentInSlot(1);
                                    double diff1 = (posY + ydiff * 4) - (posY + ydiff * 3 + 2);
                                    if (mc.player.getDistanceToEntity(player) < 10) {
                                        RenderUtil.renderItem(stack4, (int) endPosX + 4, (int) (posY + ydiff * 4) - (int) (diff1 / 2) - 18);
                                        //mc.smallfontRenderer.drawStringWithShadow(stackname, (float) endPosX + 5, (float) ((posY + ydiff * 4) - (diff1 / 2)) - (mc.clickguismall.getStringHeight(stack4.getMaxDamage() - stack4.getItemDamage() + "") / 2), color);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.enableBlend();
        entityRenderer.setupOverlayRendering();
    }

    @EventTarget
    public void onRenderName(EventRenderName eventRenderName) {
        if (!getState())
            return;
        eventRenderName.setCancelled(tags.getValue());
    }

    private boolean isValid(Entity entity) {
        if (mc.gameSettings.thirdPersonView == 0 && entity == mc.player)
            return false;
        if (entity.isDead)
            return false;
        if ((entity instanceof net.minecraft.entity.passive.EntityAnimal))
            return false;
        if ((entity instanceof EntityPlayer))
            return true;
        if ((entity instanceof EntityArmorStand))
            return false;
        if ((entity instanceof IAnimals))
            return false;
        if ((entity instanceof EntityItemFrame))
            return false;
        if ((entity instanceof EntityArrow || entity instanceof EntitySpectralArrow))
            return false;
        if ((entity instanceof EntityMinecart))
            return false;
        if ((entity instanceof EntityBoat))
            return false;
        if ((entity instanceof EntityDragonFireball))
            return false;
        if ((entity instanceof EntityXPOrb))
            return false;
        if ((entity instanceof EntityMinecartChest))
            return false;
        if ((entity instanceof EntityTNTPrimed))
            return false;
        if ((entity instanceof EntityMinecartTNT))
            return false;
        if ((entity instanceof EntityVillager))
            return false;
        if ((entity instanceof EntityExpBottle))
            return false;
        if ((entity instanceof EntityLightningBolt))
            return false;
        if ((entity instanceof EntityPotion))
            return false;
        if ((entity instanceof Entity))
            return false;
        if (((entity instanceof net.minecraft.entity.monster.EntityMob || entity instanceof net.minecraft.entity.monster.EntitySlime || entity instanceof net.minecraft.entity.boss.EntityDragon
                || entity instanceof net.minecraft.entity.monster.EntityGolem)))
            return false;
        return entity != mc.player;
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, this.modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, this.projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, this.viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.vector))
            return new Vector3d((this.vector.get(0) / scaleFactor), ((Display.getHeight() - this.vector.get(1)) / scaleFactor), this.vector.get(2));
        return null;
    }
}