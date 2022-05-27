package ru.neverhook.feature.combat;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventAttackPacket;
import ru.neverhook.event.impl.EventReceivePacket;
import ru.neverhook.event.impl.EventStep;
import ru.neverhook.event.impl.EventUpdate;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.ui.clickgui.settings.Setting;
import ru.neverhook.utils.other.TimerUtils;

import java.util.ArrayList;

public class Criticals extends Feature {

    private final Setting jump;
    int stage, count;
    TimerUtils lastStep = new TimerUtils();
    TimerUtils ncpTimer = new TimerUtils();
    int time = 0;
    double[] offsets = new double[]{0.41000000000000017, 0.01099999999999924};

    public Criticals() {
        super("Criticals", Category.Combat);
        ArrayList<String> mode = new ArrayList<>();
        mode.add("NCP");
        mode.add("Packet");
        Main.instance.setmgr.addSetting(new Setting("Criticals Mode", this, "Packet", mode));
        Main.instance.setmgr.addSetting(this.jump = new Setting("Mini-Jump", this, false));
    }

    @EventTarget
    public void onAttackPacket(EventAttackPacket event) {
        String mode = Main.instance.setmgr.getSettingByName("Criticals Mode").getValString();
        if (KillAura.target != null && Main.instance.featureManager.getFeatureByClass(KillAura.class).getState()) {
            double x = mc.player.posX;
            double y = mc.player.posY;
            double z = mc.player.posZ;
            if (mode.equalsIgnoreCase("Packet")) {
                if (jump.getValue()) {
                    mc.player.setPosition(x, y + 0.04, z);
                }
                mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.11, z, false));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.1100013579, z, false));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 1.3579E-6, z, false));
                mc.player.onCriticalHit(event.getTargetEntity());
            } else if (mode.equalsIgnoreCase("NCP")) {
                if (ncpTimer.hasReached(800L) && mc.player.onGround) {
                    if (jump.getValue()) {
                        mc.player.setPosition(x, y + 0.04, z);
                    }
                    for (double offset : offsets) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset, mc.player.posZ, false));
                    }
                    mc.player.onCriticalHit(event.getTargetEntity());
                    ncpTimer.reset();
                }
            }
        }
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        Packet<?> packet = event.getPacket();

        if (packet instanceof SPacketPlayerPosLook) {
            stage = 0;
        }
        if (packet instanceof CPacketConfirmTransaction) {
            CPacketConfirmTransaction confirmTransaction = (CPacketConfirmTransaction) packet;
            boolean accepted = confirmTransaction.isAccepted();
            int uid = confirmTransaction.getUid();
            if (accepted && uid == 0) {
                count++;
            }
        }
    }

    @EventTarget
    public void onStep(EventStep event) {
        if (!event.isPre()) {
            lastStep.reset();
            if (!mc.player.isCollidedHorizontally) {
                stage = 0;
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("Criticals Mode").getValString();
        this.setSuffix(mode);
        time++;
    }
}
