package ru.neverhook.feature.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import optifine.CustomColors;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.Event2D;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.utils.animation.AnimationUtil;
import ru.neverhook.utils.animation.Translate;
import ru.neverhook.utils.movement.MovementUtil;
import ru.neverhook.utils.other.TimerUtils;
import ru.neverhook.utils.visual.ColorUtils;
import ru.neverhook.utils.visual.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class HUD extends Feature {

    public static String clientName = "NeverHook";
    public static float count = 0.0F;
    public static float globalOffset;
    public TimerUtils timer = new TimerUtils();
    float animation = 0;

    public HUD() {
        super("HUD", Category.Hud);
    }

    private static Feature getNextEnabledModule(ArrayList<Feature> features, int startingIndex) {
        for (int i = startingIndex; i < features.size(); i++) {
            Feature feature = features.get(i);
            if (feature.getState() && feature.visible) {
                if (!feature.getDisplayName().equals("ClickGui") && feature.visible) {
                    return feature;
                }
            }
        }
        return null;
    }

    public static void onRenderPotionStatus(ScaledResolution scaledResolution) {
        float offset = globalOffset;
        float pY = -2;

        List<PotionEffect> potions = new ArrayList<>(mc.player.getActivePotionEffects());
        potions.sort(Comparator.comparingDouble(effect -> mc.fontRendererObj.getStringWidth((Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName()))));

        for (PotionEffect effect : potions) {
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

            int getPotionColor = -1;
            if ((effect.getDuration() < 200)) {
                getPotionColor = new Color(215, 59, 59).getRGB();
            } else if (effect.getDuration() < 400) {
                getPotionColor = new Color(231, 143, 32).getRGB();
            } else if (effect.getDuration() > 400) {
                getPotionColor = new Color(172, 171, 171).getRGB();
            }
            if (ClientFont.defaultFont.getValue()) {
                mc.fontRendererObj.drawStringWithShadow(name, scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(name + PType) - 3, scaledResolution.getScaledHeight() - 28 + pY - offset, potion.getLiquidColor());
                mc.fontRendererObj.drawStringWithShadow(PType, scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(PType) - 2, scaledResolution.getScaledHeight() - 28 + pY - offset, getPotionColor);
            } else {
                Main.getFontRender().drawStringWithShadow(name, scaledResolution.getScaledWidth() - Main.getFontRender().getStringWidth(name + PType) - 3, scaledResolution.getScaledHeight() - 28 + pY - offset, potion.getLiquidColor());
                Main.getFontRender().drawStringWithShadow(PType, scaledResolution.getScaledWidth() - Main.getFontRender().getStringWidth(PType) - 2, scaledResolution.getScaledHeight() - 28 + pY - offset, getPotionColor);
            }
            pY -= 11;
        }
    }

    public static void onRenderArrayList(ScaledResolution scaledResolution) {
        double width = scaledResolution.getScaledWidth() - (ArreyList.rectRight.getValue() ? 1 : 0);
        float yDist = 1;
        int yTotal = 0;
        if (Main.instance.featureManager.getFeatureByClass(ArreyList.class).getState()) {
            Main.instance.featureManager.getFeatureList().sort(Comparator.comparing(module -> !ClientFont.defaultFont.getValue() ? -Main.getFontRender().getStringWidth(module.getDisplayName()) : -mc.fontRendererObj.getStringWidth(module.getDisplayName())));
            for (Feature feature : Main.instance.featureManager.getFeatureList()) {
                if (!feature.getDisplayName().equals("ClickGui")) {
                    Translate translate = feature.getTranslate();
                    String moduleLabel = feature.getDisplayName();
                    float listOffset = ArreyList.height.getValFloat();
                    float length = !ClientFont.defaultFont.getValue() ? Main.getFontRender().getStringWidth(moduleLabel) : mc.fontRendererObj.getStringWidth(moduleLabel);
                    float featureX = (float) (width - length);
                    boolean enable = feature.getState() && feature.visible;
                    if (enable) {
                        translate.arrayListAnim(featureX, yDist, (float) (0.1f * Minecraft.frameTime) / 6, (float) (0.3f * Minecraft.frameTime) / 6);
                    } else {
                        translate.arrayListAnim((float) (width), yDist, (float) (0.1f * Minecraft.frameTime) / 6, (float) (0.3f * Minecraft.frameTime) / 6);
                    }

                    int yPotion = 0;

                    for (PotionEffect potion : mc.player.getActivePotionEffects()) {
                        if (potion.getPotion().isBeneficial()) {
                            yPotion = 26;
                        }
                        if (potion.getPotion().isBadEffect()) {
                            yPotion = 26 * 2;
                        }
                    }

                    double translateY = translate.getY() + yPotion;
                    double translateX = translate.getX() - (ArreyList.rectRight.getValue() ? 2.5 : 1.5);
                    int color = 0;
                    int onecolor = ArreyList.onecolor.getColorValue();
                    int twoColor = ArreyList.twocolor.getColorValue();
                    double time = ArreyList.time.getValDouble();
                    String mode = Main.instance.setmgr.getSettingByName("ArrayList Color").getValString();
                    boolean visible = translate.getX() < width;
                    if (visible) {
                        switch (mode.toLowerCase()) {
                            case "rainbow":
                                color = ColorUtils.rainbowNew((int) (yDist * 200 * 0.1f), 0.8f, 1.0f);
                                break;
                            case "astolfo":
                                color = ColorUtils.astolfo((int) yDist, yTotal);
                                break;
                            case "pulse":
                                color = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / time) / 100.0 + 6.0F * (yDist * 2.55) / 60).getRGB();
                                break;
                            case "custom":
                                color = ColorUtils.TwoColoreffect(new Color(onecolor), new Color(twoColor), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yDist * 2.55) / 60).getRGB();
                                break;
                            case "none":
                                color = -1;
                                break;
                            case "category":
                                color = feature.getCategory().getColor();
                                break;
                        }
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(-ArreyList.x.getValDouble(), ArreyList.y.getValDouble(), 1.0D);
                        int back = (int) Main.instance.setmgr.getSettingByName("BackgroundAplha").getValDouble();
                        int back2 = (int) Main.instance.setmgr.getSettingByName("BackgroundBright").getValDouble();
                        if (ArreyList.backGround.getValue()) {
                            RenderUtil.drawRect(translateX - 2D, translateY - 1D, width, translateY + listOffset - 1D, ColorUtils.getColor(back, back2));
                        }
                        if (ArreyList.border.getValue()) {
                            RenderUtil.drawRect(translateX - 2.6D, translateY - 1, translateX - 2D, translateY + listOffset - 1D, color);
                        }
                        Feature nextModule = null;
                        int nextIndex = Main.instance.featureManager.getFeatureList().indexOf(feature) + 1;
                        if (Main.instance.featureManager.getFeatureList().size() > nextIndex) {
                            nextModule = getNextEnabledModule(Main.instance.featureManager.getFeatureList(), nextIndex);
                        }
                        if (nextModule != null) {
                            double font = !ClientFont.defaultFont.getValue() ? Main.getFontRender().getStringWidth(nextModule.getDisplayName()) : mc.fontRendererObj.getStringWidth(nextModule.getDisplayName());
                            double dif = (length - font);
                            if (ArreyList.border.getValue()) {
                                RenderUtil.drawRect(translateX - 2.6D, translateY + listOffset - 1D, translateX - 2.6D + dif, translateY + (double) listOffset - 0.6D, color);
                            }
                        } else {
                            if (ArreyList.border.getValue()) {
                                RenderUtil.drawRect(translateX - 2.6D, translateY + listOffset - 1D, width, translateY + (double) listOffset - 0.6, color);
                            }
                        }

                        if (!ClientFont.defaultFont.getValue()) {
                            String modeArrayFont = Main.instance.setmgr.getSettingByName("FontList").getValString();
                            float y = modeArrayFont.equalsIgnoreCase("Verdana") ? 0.5f : modeArrayFont.equalsIgnoreCase("SF UI") ? 1.3f : modeArrayFont.equalsIgnoreCase("Bebas") ? 3.5f : 2.1f;
                            if (!ClientFont.defaultFont.getValue()) {
                                Main.getFontRender().drawStringWithShadow(moduleLabel, translateX, translateY + y, color);
                            }
                        } else {
                            mc.fontRendererObj.drawStringWithShadow(moduleLabel, (float) translateX, (float) translateY + 1F, color);
                        }

                        if (ArreyList.rectRight.getValue()) {
                            RenderUtil.drawRect(width, translateY - 1, width + 1, translateY + listOffset - 1.0D, color);
                        }

                        yDist += listOffset;

                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }

    @EventTarget
    public void onRender2D(Event2D e) {
        float target = (mc.currentScreen instanceof GuiChat) ? 15 : 0;
        float delta = globalOffset - target;
        globalOffset -= delta / Math.max(1, Minecraft.getDebugFPS()) * 10;
        if (!Double.isFinite(globalOffset)) {
            globalOffset = 0;
        }
        if (globalOffset > 15) {
            globalOffset = 15;
        }
        if (globalOffset < 0) {
            globalOffset = 0;
        }
        onRenderWaterMark();
        onRenderHotbar();
        onRenderPotionStatus(e.getResolution());
    }

    public void onRenderWaterMark() {
        mc.robotoRegular.drawStringWithOutline("N", 3, 3, Main.getClientColor().brighter().getRGB());
        mc.robotoRegular.drawStringWithOutline("ever", 10, 3, -1);
        mc.robotoRegular.drawStringWithOutline("H", 30, 3, Main.getClientColor().brighter().getRGB());
        mc.robotoRegular.drawStringWithOutline("ook", 37, 3, -1);
    }

    public void onRenderHotbar() {
        ScaledResolution sr = new ScaledResolution(mc);
        animation = AnimationUtil.animation(animation, mc.currentScreen instanceof GuiChat ? sr.getScaledHeight() - 22 : sr.getScaledHeight() - 9, 0.0001F);
        String speed = String.format("%.2f blocks/sec", MovementUtil.getSpeed() * 16);
        String text = "§7Build §7-§f " + Main.build;
        int ping = mc.isSingleplayer() ? 0 : Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
        if (ClientFont.defaultFont.getValue()) {
            mc.fontRendererObj.drawStringWithShadow("" + Math.round(mc.player.posX) + ", " + Math.round(mc.player.posY) + ", " + Math.round(mc.player.posZ), 2, (int) animation, -1);
            mc.fontRendererObj.drawStringWithShadow("Ping: §7" + ping + "ms", sr.getScaledWidth() - mc.fontRendererObj.getStringWidth("Ping: §7" + ping + "ms") - 2, animation + -10, -1);
            mc.fontRendererObj.drawStringWithShadow("FPS: §7" + Minecraft.getDebugFPS(), 2, animation + -18, -1);
            mc.fontRendererObj.drawStringWithShadow(speed, 2, animation + -9, -1);
            mc.fontRendererObj.drawStringWithShadow(text, sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(text) - 2, animation, -1);
        } else {
            Main.getFontRender().drawStringWithShadow("" + Math.round(mc.player.posX) + ", " + Math.round(mc.player.posY) + ", " + Math.round(mc.player.posZ), 2, (int) animation, -1);
            Main.getFontRender().drawStringWithShadow("Ping: §7" + ping + "ms", sr.getScaledWidth() - Main.getFontRender().getStringWidth("Ping: §7" + ping + "ms") - 2, animation + -10, -1);
            Main.getFontRender().drawStringWithShadow("FPS: §7" + Minecraft.getDebugFPS(), 2, animation + -18, -1);
            Main.getFontRender().drawStringWithShadow(speed, 2, animation + -9, -1);
            Main.getFontRender().drawStringWithShadow(text, sr.getScaledWidth() - Main.getFontRender().getStringWidth(text) - 2, animation, -1);
        }
    }
}