package ru.neverhook.feature.world;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventInsideBlock;
import ru.neverhook.event.impl.EventPushOutBlock;
import ru.neverhook.event.impl.EventUpdateLiving;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;

public class FreeCam extends Feature {

    public Setting speed;
    private float yaw;
    private float pitch;
    private float old;
    private double oldX;
    private double oldY;
    private double oldZ;

    public FreeCam() {
        super("FreeCam", Category.World);
        Main.instance.setmgr.addSetting(speed = new Setting("Speed", this, 0.1, 0.01, 10, 0.1));
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(this.old);
        mc.player.rotationPitch = this.pitch;
        mc.player.rotationYaw = this.yaw;
        mc.player.noClip = false;
        mc.renderGlobal.loadRenderers();
        mc.player.noClip = false;
        mc.player.setPositionAndRotation(oldX, oldY, oldZ, mc.player.rotationYaw, mc.player.rotationPitch);
        mc.world.removeEntityFromWorld(-69);
        EntityOtherPlayerMP fakePlayer = null;
        mc.player.motionZ = 0;
        mc.player.motionX = 0;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        oldX = mc.player.posX;
        oldY = mc.player.posY;
        oldZ = mc.player.posZ;
        mc.player.noClip = true;
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.posY -= 0;
        fakePlayer.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-69, fakePlayer);
        super.onEnable();
    }

    @EventTarget
    public void onInsideBlock(EventInsideBlock event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onPushOutBlock(EventPushOutBlock event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onUpdate(EventUpdateLiving event) {
        mc.player.noClip = true;
        mc.player.onGround = false;
        mc.player.capabilities.setFlySpeed((float) speed.getValDouble());
        mc.player.capabilities.isFlying = true;
    }
}
