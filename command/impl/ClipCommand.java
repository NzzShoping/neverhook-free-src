package ru.neverhook.command.impl;

import net.minecraft.client.Minecraft;
import ru.neverhook.command.AbstractCommand;

public class ClipCommand extends AbstractCommand {

    Minecraft mc = Minecraft.getMinecraft();

    public ClipCommand() {
        super("clip", "clip / hclip", "§6.clip / (hclip) + / - §3<value>", "clip", "hclip");
    }

    @Override
    public void execute(String... args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("clip")) {
                try {
                    if (args[1].equals("+")) {
                        mc.player.setPosition(mc.player.posX, mc.player.posY + Double.parseDouble(args[2]), mc.player.posZ);
                    }
                    if (args[1].equals("-")) {
                        mc.player.setPosition(mc.player.posX, mc.player.posY - Double.parseDouble(args[2]), mc.player.posZ);
                    }
                } catch (Exception ex) {
                }
            }
            if (args[0].equalsIgnoreCase("hclip")) {
                double x = mc.player.posX;
                double y = mc.player.posY;
                double z = mc.player.posZ;
                double yaw = mc.player.rotationYaw * 0.017453292;
                try {
                    if (args[1].equals("+")) {
                        mc.player.setPosition(x - Math.sin(yaw) * Double.parseDouble(args[2]), y, z + Math.cos(yaw) * Double.parseDouble(args[2]));
                    }
                    if (args[1].equals("-")) {
                        mc.player.setPosition(x + Math.sin(yaw) * Double.parseDouble(args[2]), y, z - Math.cos(yaw) * Double.parseDouble(args[2]));
                    }
                } catch (Exception ex) {

                }
            }
        } else {
            this.usage();
        }
    }
}
