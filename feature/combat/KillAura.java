package ru.neverhook.feature.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import optifine.CustomColors;
import org.lwjgl.opengl.GL11;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.*;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.feature.world.StreamerMode;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.animation.AnimationUtil;
import ru.neverhook.utils.combat.KillauraUtil;
import ru.neverhook.utils.combat.RotationUtil;
import ru.neverhook.utils.entity.EntityUtil;
import ru.neverhook.utils.inventory.InventoryUtil;
import ru.neverhook.utils.movement.MovementUtil;
import ru.neverhook.utils.other.MathUtils;
import ru.neverhook.utils.other.TimerUtils;
import ru.neverhook.utils.visual.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class KillAura extends Feature {

    /* UTILS */

    public static boolean canBlock;

    /* SETTINGS */

    public static Setting players;
    public static Setting mobs;
    public static Setting invis;
    public static Setting rayTrace;
    public static Setting walls;

    public static EntityLivingBase target;
    public static TimerUtils timer;
    public static Setting range;
    public static Setting fov;
    public static Setting onlyCrit;
    public static Setting wellmore;
    private final TimerUtils shieldTimer = new TimerUtils();

    /* CONSTANTS */
    List<EntityLivingBase> targets;
    private double healthBarWidth;

    public KillAura() {
        super("KillAura", Category.Combat);
        this.targets = new ArrayList<>();

        ArrayList<String> rotation = new ArrayList<>();
        rotation.add("Packet");
        rotation.add("Client");
        rotation.add("None");
        Main.instance.setmgr.addSetting(new Setting("Rotation Mode", this, "Packet", rotation));

        ArrayList<String> targetH = new ArrayList<>();
        targetH.add("Astolfo");
        targetH.add("Dev");
        Main.instance.setmgr.addSetting(new Setting("TargetHud Mode", this, "Astolfo", targetH));

        ArrayList<String> sort = new ArrayList<>();
        sort.add("Distance");
        sort.add("Health");
        Main.instance.setmgr.addSetting(new Setting("TargetSort Mode", this, "Distance", sort));

        ArrayList<String> attackMethod = new ArrayList<>();
        attackMethod.add("Wellmore");
        attackMethod.add("Other");
        Main.instance.setmgr.addSetting(new Setting("Attack Mode", this, "Wellmore", attackMethod));

        Main.instance.setmgr.addSetting(fov = new Setting("FOV", this, 360.0, 0.0, 360.0, 1));
        Main.instance.setmgr.addSetting(range = new Setting("AttackRange", this, 4.0, 3.0, 7.0, 0.1));
        Main.instance.setmgr.addSetting(players = new Setting("Players", this, true));
        Main.instance.setmgr.addSetting(mobs = new Setting("Mobs", this, false));
        Main.instance.setmgr.addSetting(invis = new Setting("Invisible", this, false));
        Main.instance.setmgr.addSetting(walls = new Setting("Walls", this, true));
        Main.instance.setmgr.addSetting(new Setting("Weapon Only", this, false));
        Main.instance.setmgr.addSetting(new Setting("Using Item Check", this, false));
        Main.instance.setmgr.addSetting(new Setting("Shield Breaker", this, false));
        Main.instance.setmgr.addSetting(new Setting("Shield Fixer", this, true));
        Main.instance.setmgr.addSetting(rayTrace = new Setting("Ray-Trace", this, false));
        Main.instance.setmgr.addSetting(wellmore = new Setting("Wellmore", this, false));
        Main.instance.setmgr.addSetting(onlyCrit = new Setting("Only Crits", this, false));
        Main.instance.setmgr.addSetting(new Setting("Crits Fall Distance", this, 0.08, 0.08, 0.5, 0.01));
        Main.instance.setmgr.addSetting(new Setting("TargetHud", this, true));
        Main.instance.setmgr.addSetting(new Setting("TargetHudPositionX", this, 70, -700, 600, 1));
        Main.instance.setmgr.addSetting(new Setting("TargetHudPositionY", this, 80, -400, 300, 1));
    }

    public static boolean doesHotbarHaveAxe() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
                return true;
            }
        }
        return false;
    }

    public static boolean doesHotbarHaveSword() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSword) {
                return true;
            }
        }
        return false;
    }

    public static void attackEntitySuccess(EntityLivingBase target) {
        float cooldown = onlyCrit.getValue() ? 0.95F : 1F;

        if (target == null)
            return;

        if (target.getHealth() > 0) {
            if (mc.player.getCooledAttackStrength(0) >= cooldown) {
                mc.playerController.attackEntity(mc.player, target);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    @Override
    public void onEnable() {
        target = null;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        target = null;
        super.onDisable();
    }

    @EventTarget
    public void onAttack(EventAttackPacket event) {
        if (mc.player.isBlocking() && mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemShield && Main.instance.setmgr.getSettingByName("Shield Fixer").getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-0.8, -0.8, -0.8), EnumFacing.DOWN));
        }
    }

    @EventTarget
    public void onFix(EventSendPacket event) {

        /* INTERACT FIX */

        if (wellmore.getValue() && this.getState()) {

            if (target == null) return;

            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                event.setCancelled(true);
            }

            if (event.getPacket() instanceof CPacketUseEntity) {
                CPacketUseEntity packetUseEntity = (CPacketUseEntity) event.getPacket();
                if ((packetUseEntity.getAction() == CPacketUseEntity.Action.INTERACT) || (packetUseEntity.getAction() == CPacketUseEntity.Action.INTERACT_AT)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventTarget
    public void onPreAttack(EventPreMotionUpdate preAttack) {
        String mode = Main.instance.setmgr.getSettingByName("Attack Mode").getValString();
        if (mode.equalsIgnoreCase("Other")) {
            doAuraPre(preAttack);
        }
    }

    @EventTarget
    public void onPostAttack(EventUpdate postAttack) {
        String mode = Main.instance.setmgr.getSettingByName("Attack Mode").getValString();
        if (mode.equalsIgnoreCase("Wellmore")) {
            doAuraPost(postAttack);
        }
    }

    private void doAuraPre(EventPreMotionUpdate event) {
        if (mc.player.isEntityAlive()) {

            /* TOGGLE OFF CHECKS */

            KillauraUtil.toggleOffChecks();

            /* VARIABLES */

            String mode = Main.instance.setmgr.getSettingByName("Rotation Mode").getValString();
            float yaw = mode.equalsIgnoreCase("Packet") ? RotationUtil.Rotation.fakeYaw : mc.player.rotationYaw;
            float pitch = mode.equalsIgnoreCase("Packet") ? RotationUtil.Rotation.fakePitch : mc.player.rotationPitch;
            this.setSuffix(mode + ", " + MathUtils.round(range.getValFloat(), 1));

            /* ENTITY SORT BY MODE */

            target = KillauraUtil.getSortEntities();

            /* ANOTHER CHECKS */

            if (target == null)
                return;

            if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && Main.instance.setmgr.getSettingByName("Weapon Only").getValue())
                return;

            if (RotationUtil.isLookingAtTarget(yaw, pitch, target, range.getValFloat()) && (Main.instance.setmgr.getSettingByName("Ray-Trace").getValue()))
                return;

            if (mc.player.isUsingItem() && Main.instance.setmgr.getSettingByName("Using Item Check").getValue()) {
                return;
            }

            /* SHIELD HELPERS */

            if (Main.instance.setmgr.getSettingByName("Shield Breaker").getValue()) {

                if (target == null)
                    return;

                if (target.isBlocking()) {
                    canBlock = true;
                    if (InventoryUtil.getAxeAtHotbar() != -1) {
                        if (!doesHotbarHaveAxe()) {
                            return;
                        }
                        mc.player.inventory.currentItem = InventoryUtil.getAxeAtHotbar();
                    }
                    if (canBlock && mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                        if (shieldTimer.hasReached(150L)) {
                            if (!doesHotbarHaveAxe()) {
                                return;
                            }
                            EntityUtil.attackEntity(target, false, false);
                            shieldTimer.reset();
                        }
                        canBlock = false;
                    }
                } else {
                    if (target.getHeldItemOffhand().getItem() instanceof ItemShield) {
                        if (InventoryUtil.getSwordAtHotbar() != -1) {
                            mc.player.inventory.currentItem = InventoryUtil.getSwordAtHotbar();
                        }
                    }
                }
            }
        }

        /* ATTACK METHOD */

        if (MovementUtil.isBlockAboveHead()) {
            if (!(mc.player.fallDistance > Main.instance.setmgr.getSettingByName("Crits Fall Distance").getValFloat() && onlyCrit.getValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb)) {
                return;
            }
        } else {
            if (mc.player.fallDistance != 0 && onlyCrit.getValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb) {
                return;
            }
        }

        attackEntitySuccess(target);
    }

    private void doAuraPost(EventUpdate event) {
        if (mc.player.isEntityAlive()) {

            /* TOGGLE OFF CHECKS */

            KillauraUtil.toggleOffChecks();

            /* VARIABLES */

            String mode = Main.instance.setmgr.getSettingByName("Rotation Mode").getValString();
            float yaw = mode.equalsIgnoreCase("Packet") ? RotationUtil.Rotation.fakeYaw : mc.player.rotationYaw;
            float pitch = mode.equalsIgnoreCase("Packet") ? RotationUtil.Rotation.fakePitch : mc.player.rotationPitch;
            this.setSuffix(mode + ", " + MathUtils.round(range.getValFloat(), 1));

            /* ENTITY SORT BY MODE */

            target = KillauraUtil.getSortEntities();

            /* ANOTHER CHECKS */

            if (target == null)
                return;

            if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && Main.instance.setmgr.getSettingByName("Weapon Only").getValue())
                return;

            if (RotationUtil.isLookingAtTarget(yaw, pitch, target, range.getValFloat()) && (Main.instance.setmgr.getSettingByName("Ray-Trace").getValue()))
                return;

            if (mc.player.isUsingItem() && Main.instance.setmgr.getSettingByName("Using Item Check").getValue()) {
                return;
            }

            /* SHIELD HELPERS */

            if (Main.instance.setmgr.getSettingByName("Shield Breaker").getValue()) {

                if (target == null)
                    return;

                if (target.isBlocking()) {
                    canBlock = true;
                    if (InventoryUtil.getAxeAtHotbar() != -1) {
                        if (!doesHotbarHaveAxe()) {
                            return;
                        }
                        mc.player.inventory.currentItem = InventoryUtil.getAxeAtHotbar();
                    }
                    if (canBlock && mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                        if (shieldTimer.hasReached(150L)) {
                            if (!doesHotbarHaveAxe()) {
                                return;
                            }
                            EntityUtil.attackEntity(target, false, false);
                            shieldTimer.reset();
                        }
                        canBlock = false;
                    }
                } else {
                    if (target.getHeldItemOffhand().getItem() instanceof ItemShield) {
                        if (InventoryUtil.getSwordAtHotbar() != -1) {
                            mc.player.inventory.currentItem = InventoryUtil.getSwordAtHotbar();
                        }
                    }
                }
            }
        }

        /* ATTACK METHOD */

        if (MovementUtil.isBlockAboveHead()) {
            if (!(mc.player.fallDistance >= Main.instance.setmgr.getSettingByName("Crits Fall Distance").getValFloat()) && !mc.player.isInLiquid() && !mc.player.isInWeb && (onlyCrit.getValue()))
                return;
        } else {
            if ((mc.player.fallDistance != 0) && !mc.player.isInLiquid() && !mc.player.isInWeb && (onlyCrit.getValue()))
                return;
        }

        attackEntitySuccess(target);
    }

    @EventTarget
    public void onRotations(EventPreMotionUpdate event) {

        /* VARIABLES */

        String mode = Main.instance.setmgr.getSettingByName("Rotation Mode").getValString();

        /* CHECKS AND ROTATIONS */

        if (target == null)
            return;

        if (target.getHealth() > 0) {

            /* ONLY SWORD CHECK */

            if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && Main.instance.setmgr.getSettingByName("Weapon Only").getValue())
                return;

            /* ROTATIONS */

            float[] rots = RotationUtil.getRotations(target, true);

            if (mode.equalsIgnoreCase("Packet")) {
                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
            } else if (mode.equalsIgnoreCase("Client")) {
                mc.player.rotationYaw = rots[0];
                mc.player.rotationPitch = rots[1];
            }
        }
    }

    @EventTarget
    public void onRenderTargetHUD(Event2D event) {

        /* TARGET HUD */

        String mode = Main.instance.setmgr.getSettingByName("TargetHud Mode").getValString();
        if (Main.instance.setmgr.getSettingByName("TargetHud").getValue() && target != null && target.getHealth() > 0) {
            if (mode.equalsIgnoreCase("Astolfo")) {
                renderAstolfoTGHUD(event.getResolution());
            } else if (mode.equalsIgnoreCase("Dev")) {
                renderDevTGHUD(event.getResolution());
            }
        }
    }

    /* TARGET HUDS */

    private void renderDevTGHUD(ScaledResolution resolution) {
        float scaledWidth = resolution.getScaledWidth();
        float scaledHeight = resolution.getScaledHeight();
        float x = scaledWidth / 2.0F - Main.instance.setmgr.getSettingByName("TargetHudPositionX").getValFloat(), y = scaledHeight / 2.0F + Main.instance.setmgr.getSettingByName("TargetHudPositionY").getValFloat();
        float x2 = scaledWidth / 2.0F - Main.instance.setmgr.getSettingByName("TargetHudPositionX").getValFloat();
        float y2 = scaledHeight / 2.0F + Main.instance.setmgr.getSettingByName("TargetHudPositionY").getValFloat();
        double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
        healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
        double check = target != null && target.getHealth() < (target instanceof EntityPlayer ? 18 : 10) && target.getHealth() > 1 ? 8 : 0;
        this.healthBarWidth = AnimationUtil.calculateCompensation((float) healthWid, (float) this.healthBarWidth, (long) 0.005, 0.005);
        RenderUtil.drawRect2(x, y, 145, 50, new Color(23, 23, 25, 203).getRGB());
        if (!target.getName().isEmpty()) {
            mc.robotoLight.drawStringWithShadow(Main.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.nameSpoof.getValue() ? "Protected" : target.getName(), x + 37, y + 5, -1);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 1);
        GlStateManager.scale(1.5F, 1.5F, 1.5F);
        GlStateManager.translate(-x - 14, -y + 14, 1);
        mc.fontRendererObj.drawStringWithShadow("§c\u2764", x + 16, y + 10, -1);
        GlStateManager.popMatrix();
        GlStateManager.color(1, 1, 1, 1);

        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 125, (int) y + 7);
        mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 125, (int) y + 1);

        ArrayList<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            ItemStack armorSlot = ((EntityPlayer) target).getEquipmentInSlot(i);
            if (armorSlot != null) {
                list.add(armorSlot);
            }
        }
        for (ItemStack itemStack : list) {
            RenderHelper.enableGUIStandardItemLighting();
            RenderUtil.renderItem(itemStack, (int) x2 + 36, (int) (y + 16));
            x2 += 16;
        }

        for (PotionEffect effect : target.getActivePotionEffects()) {
            Potion potion = Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()));
            assert potion != null;
            String name = I18n.format(potion.getName());
            String PType = "";
            if (effect.getAmplifier() == 1) {
                name = name + " 2";
            } else if (effect.getAmplifier() == 2) {
                name = name + " 3";
            } else if (effect.getAmplifier() == 3) {
                name = name + " 4";
            }
            if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
                PType = PType + " " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = PType + " " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = PType + " " + Potion.getDurationString(effect);
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableBlend();
            mc.fontRendererObj.drawStringWithShadow(name + ": " + ChatFormatting.GRAY + PType, x + 1, y2 - 9, potion.getLiquidColor());
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.popMatrix();
            y2 -= 10;
        }

        for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
            if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                Gui.drawScaledCustomSizeModalRect((int) x + 1, (int) y + 1, 8.0f, 8.0f, 8, 8, 34, 34, 64.0f, 64.0f);
                GlStateManager.bindTexture(0);
            }
            GL11.glDisable(3089);
        }

        RenderUtil.drawRect2(x + 18, y + 41, 120, 3, new Color(20, 221, 32).darker().darker().darker().getRGB());
        RenderUtil.drawRect2(x + 18, y + 41, healthBarWidth + check, 3, new Color(new Color(253, 174, 46).darker().getRGB()).getRGB());
        RenderUtil.drawRect2(x + 18, y + 41, healthWid, 3, new Color(new Color(20, 221, 32).getRGB()).getRGB());
    }

    private void renderAstolfoTGHUD(ScaledResolution resolution) {
        float scaledWidth = resolution.getScaledWidth();
        float scaledHeight = resolution.getScaledHeight();
        float x = scaledWidth / 2.0F - Main.instance.setmgr.getSettingByName("TargetHudPositionX").getValFloat(), y = scaledHeight / 2.0F + Main.instance.setmgr.getSettingByName("TargetHudPositionY").getValFloat();
        double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
        healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
        double check = target != null && target.getHealth() < (target instanceof EntityPlayer ? 18 : 10) && target.getHealth() > 1 ? 8 : 0;
        this.healthBarWidth = AnimationUtil.calculateCompensation((float) healthWid, (float) this.healthBarWidth, (long) 0.005, 0.005);
        RenderUtil.drawRect2(x, y, 155, 60, new Color(20, 20, 20, 200).getRGB());
        if (!target.getName().isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow(Main.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.nameSpoof.getValue() ? "Protected" : target.getName(), x + 31, y + 5, -1);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 1);
        GL11.glScalef(2.5f, 2.5f, 2.5f);
        GlStateManager.translate(-x - 3, -y - 2, 1);
        mc.fontRendererObj.drawStringWithShadow(MathUtils.round((target.getHealth() / 2.0f), 1) + " \u2764", x + 16, y + 10, new Color(Main.getClientColor().getRGB()).getRGB());
        GlStateManager.popMatrix();
        GlStateManager.color(1, 1, 1, 1);

        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 7);
        mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 1);

        GuiInventory.drawEntityOnScreen(x + 16, y + 55, 25, target.rotationYaw, -target.rotationPitch, target);
        RenderUtil.drawRect2(x + 30, y + 48, 120, 8, new Color(Main.getClientColor().getRGB()).darker().darker().darker().getRGB());
        RenderUtil.drawRect2(x + 30, y + 48, healthBarWidth + check, 8, new Color(Main.getClientColor().getRGB()).darker().darker().getRGB());
        RenderUtil.drawRect2(x + 30, y + 48, healthWid, 8, new Color(Main.getClientColor().getRGB()).getRGB());
    }
}