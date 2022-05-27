package ru.neverhook.utils.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import ru.neverhook.event.impl.EventMove;
import ru.neverhook.utils.other.MinecraftHelper;

import java.util.Objects;

public class MovementUtil implements MinecraftHelper {

    public static float getDirection() {
        Minecraft mc = Minecraft.getMinecraft();
        float yaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0.0F) {
            yaw += 180.0F;
        }

        float forward = 1.0F;

        if (mc.player.moveForward < 0.0F) {
            forward = -0.5F;
        } else if (mc.player.moveForward > 0.0F) {
            forward = 0.5F;
        }

        if (mc.player.moveStrafing > 0.0F) {
            yaw -= 90.0F * forward;
        }

        if (mc.player.moveStrafing < 0.0F) {
            yaw += 90.0F * forward;
        }

        yaw *= 0.017453292F;
        return yaw;
    }

    public static void setMotion(double speed, float yawVec, double strafe, double forward) {
        float yaw = yawVec;
        if (forward != 0) {
            if (strafe > 0) {
                yaw += -45;
            } else if (strafe < 0) {
                yaw += 45;
            }
            strafe = 0;
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }
        if (strafe > 0) {
            strafe = 1;
        } else if (strafe < 0) {
            strafe = -1;
        }
        double motionX = Math.cos(Math.toRadians((yaw + 90.0F)));
        double motionZ = Math.sin(Math.toRadians((yaw + 90.0F)));
        mc.player.motionX = (forward * speed * motionX + strafe * speed * motionZ);
        mc.player.motionZ = (forward * speed * motionZ - strafe * speed * motionX);
    }

    public static void strafe() {
        if (mc.gameSettings.keyBindBack.isKeyDown())
            return;
        strafe(getSpeed());
    }

    public static float getSpeed() {
        return (float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
    }

    public static void setSpeed(double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0.0D && strafe == 0.0D) {
            mc.player.motionX = 0.0D;
            mc.player.motionZ = 0.0D;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += ((forward > 0.0D) ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += ((forward > 0.0D) ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1.0D;
                } else if (forward < 0.0D) {
                    forward = -1.0D;
                }
            }

            double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
            double sin = Math.sin(Math.toRadians((yaw + 90.0F)));

            mc.player.motionX = forward * speed * cos + strafe * speed * sin;
            mc.player.motionZ = forward * speed * sin - strafe * speed * cos;
        }
    }

    public static void setSpeed(EventMove event, double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0.0D && strafe == 0.0D) {
            event.setX(0.0D);
            event.setZ(0.0D);
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            double cos = Math.cos(Math.toRadians(yaw + 90.0F));
            double sin = Math.sin(Math.toRadians(yaw + 90.0F));

            event.setX(forward * speed * cos + strafe * speed * sin);
            event.setZ(forward * speed * sin - strafe * speed * cos);
        }
    }

    public static void strafe(float speed) {
        if (!mc.player.isMoving())
            return;
        double yaw = getDirection();
        mc.player.motionX = -Math.sin(yaw) * speed;
        mc.player.motionZ = Math.cos(yaw) * speed;
    }

    public static double getPlayerMoveDir() {
        Minecraft mc = Minecraft.getMinecraft();
        double xspeed = mc.player.motionX;
        double zspeed = mc.player.motionZ;
        double direction = Math.atan2(xspeed, zspeed) / Math.PI * 180.0D;
        return -direction;
    }

    public static boolean isBlockAboveHead() {
        AxisAlignedBB bb = new AxisAlignedBB(mc.player.posX - 0.3, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ + 0.3, mc.player.posX + 0.3, mc.player.posY + (!mc.player.onGround ? 1.5 : 2.5), mc.player.posZ - 0.3);
        return mc.world.getCollisionBoxes(mc.player, bb).isEmpty();
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(1)))) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
}
