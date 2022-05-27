package ru.neverhook.feature.world;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.neverhook.Main;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventReceivePacket;
import ru.neverhook.feature.Category;
import ru.neverhook.feature.Feature;
import ru.neverhook.feature.combat.KillAura;
import ru.neverhook.feature.combat.TargetStrafe;
import ru.neverhook.feature.movement.Fly;
import ru.neverhook.feature.movement.Jesus;
import ru.neverhook.feature.movement.Speed;
import ru.neverhook.feature.movement.Timer;
import ru.neverhook.feature.player.NoWeb;
import ru.neverhook.ui.notification.NotificationManager;
import ru.neverhook.ui.notification.NotificationType;

public class FlagDetector extends Feature {

    public boolean disable;

    public FlagDetector() {
        super("LagBack", Category.World);
    }

    @EventTarget
    public void packetIn(EventReceivePacket eventPacketReceive) {
        if (!this.getState())
            return;

        if (eventPacketReceive.getPacket() instanceof SPacketPlayerPosLook) {
            if (Main.instance.featureManager.getFeatureByClass(Speed.class).getState()) {
                alert("Speed");
                Main.instance.featureManager.getFeatureByClass(Speed.class).toggle();
            } else if (Main.instance.featureManager.getFeatureByClass(Fly.class).getState()) {
                alert("Fly");
                Main.instance.featureManager.getFeatureByClass(Fly.class).toggle();
            } else if (Main.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() && KillAura.target != null) {
                alert("TargetStrafe");
                Main.instance.featureManager.getFeatureByClass(TargetStrafe.class).toggle();
            } else if (Main.instance.featureManager.getFeatureByClass(NoWeb.class).getState() && mc.player.isInWeb) {
                alert("NoWeb");
                Main.instance.featureManager.getFeatureByClass(NoWeb.class).toggle();
            } else if (Main.instance.featureManager.getFeatureByClass(Jesus.class).getState() && mc.player.isInLiquid()) {
                alert("Jesus");
                Main.instance.featureManager.getFeatureByClass(Jesus.class).toggle();
            } else if (Main.instance.featureManager.getFeatureByClass(Timer.class).getState()) {
                alert("Timer");
                Main.instance.featureManager.getFeatureByClass(Timer.class).toggle();
            }
        }
    }

    public void alert(String module) {
        NotificationManager.queue("Module", "Disabling " + module + " due to lag back", 2, NotificationType.WARNING);
    }
}
