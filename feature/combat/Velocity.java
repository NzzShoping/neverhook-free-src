package ru.neverhook.feature.combat;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventReceivePacket;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

import java.util.ArrayList;
import java.util.Objects;

public class Velocity extends Feature {

    private final Setting cancelOtherDamage;
    private boolean groundCheck;

    public Velocity() {
        super("Velocity", Category.Combat);
        ArrayList<String> mode = new ArrayList<>();
        mode.add("Packet");
        mode.add("CustomMotion");
        mode.add("Matrix");
        Main.instance.setmgr.addSetting(new Setting("Velocity mode", this, "Packet", mode));
        Main.instance.setmgr.addSetting(new Setting("Vertical Percentage", this, 0, 0, 100, 1));
        Main.instance.setmgr.addSetting(new Setting("Horizontal Percentage", this, 0, 0, 100, 1));
        Main.instance.setmgr.addSetting(this.cancelOtherDamage = new Setting("Cancel Other Damage", this, true));
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        String mode = Main.instance.setmgr.getSettingByName("Velocity Mode").getValString();
        if (cancelOtherDamage.getValue()) {
            if (mc.player.hurtTime > 0) {
                if (event.getPacket() instanceof SPacketEntityVelocity) {
                    if (mc.player.isPotionActive(MobEffects.POISON) || (mc.player.isPotionActive(MobEffects.WITHER) || mc.player.isBurning())) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (!mode.equalsIgnoreCase("CustomMotion")) {
            this.setSuffix(mode);
        }
        if (mode.equalsIgnoreCase("Packet")) {
            if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) {
                if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()) {
                    event.setCancelled(true);
                }
            }
        } else if (mode.equalsIgnoreCase("CustomMotion")) {
            double hori = Main.instance.setmgr.getSettingByName("Horizontal Percentage").getValDouble();
            double vert = Main.instance.setmgr.getSettingByName("Vertical Percentage").getValDouble();
            if (mode.equalsIgnoreCase("CustomMotion")) {
                this.setSuffix("H: " + hori + "%" + " V: " + vert + "%");
                if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()) {
                    if (event.getPacket() instanceof SPacketEntityVelocity) {
                        Entity entity = Objects.requireNonNull(mc.getConnection()).clientWorldController.getEntityByID(((SPacketEntityVelocity) event.getPacket()).getEntityID());
                        if (entity instanceof EntityPlayerSP) {
                            if (hori == 0 && vert == 0) {
                                event.setCancelled(true);
                                return;
                            }
                            if (hori != 100) {
                                ((SPacketEntityVelocity) event.getPacket()).motionX = (int) (((SPacketEntityVelocity) event.getPacket()).motionX / 100 * hori);
                                ((SPacketEntityVelocity) event.getPacket()).motionZ = (int) (((SPacketEntityVelocity) event.getPacket()).motionZ / 100 * hori);
                            }

                            if (vert != 100) {
                                ((SPacketEntityVelocity) event.getPacket()).motionY = (int) (((SPacketEntityVelocity) event.getPacket()).motionY / 100 * vert);
                            }
                        }
                    }
                    if (event.getPacket() instanceof SPacketExplosion) {
                        if (hori == 0 && vert == 0) {
                            event.setCancelled(true);
                            return;
                        }
                        if (hori != 100) {
                            ((SPacketExplosion) event.getPacket()).motionX = (int) (((SPacketExplosion) event.getPacket()).motionX / 100 * hori);
                            ((SPacketExplosion) event.getPacket()).motionZ = (int) (((SPacketExplosion) event.getPacket()).motionZ / 100 * hori);
                        }
                        if (vert != 100) {
                            ((SPacketExplosion) event.getPacket()).motionY = (int) (((SPacketExplosion) event.getPacket()).motionY / 100 * vert);
                        }
                    }
                }
            }
        }
    }
}