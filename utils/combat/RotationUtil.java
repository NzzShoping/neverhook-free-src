package ru.neverhook.utils.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import ru.neverhook.event.EventTarget;
import ru.neverhook.event.impl.EventMotion;
import ru.neverhook.event.impl.EventReceivePacket;
import ru.neverhook.utils.movement.MovementUtil;
import ru.neverhook.utils.other.MinecraftHelper;
import ru.neverhook.utils.other.RandomUtils;

public class RotationUtil implements MinecraftHelper {

    public static float[] getFacePosRemote(Vec3d src, Vec3d dest) {
        double diffX = dest.xCoord - src.xCoord;
        double diffY = dest.yCoord - (src.yCoord);
        double diffZ = dest.zCoord - src.zCoord;
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0F / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0F / Math.PI);
        return new float[]{RotationUtil.updateRotation(mc.player.rotationYaw, yaw, 360), RotationUtil.updateRotation(mc.player.rotationPitch, pitch, 360)};
    }

    public static float[] getFacePosEntityRemote(EntityLivingBase facing, Entity en) {
        if (en == null) {
            return new float[]{facing.rotationYaw, facing.rotationPitch};
        }
        return getFacePosRemote(new Vec3d(facing.posX, facing.posY, facing.posZ), new Vec3d(en.posX, en.posY, en.posZ));
    }

    public static float getAngleDifference(float dir, float yaw) {
        float f = Math.abs(yaw - dir) % 360F;
        return f > 180F ? 360F - f : f;
    }

    public static float[] getLookAngles(Vec3d vec) {
        float[] angles = new float[2];
        angles[0] = (float) (Math.atan2(mc.player.posZ - vec.zCoord, mc.player.posX - vec.xCoord) / Math.PI * 180.0D) + 90.0F;
        float heights = (float) (mc.player.posY + mc.player.getEyeHeight() - vec.yCoord);
        float distance = (float) Math.sqrt((mc.player.posZ - vec.zCoord) * (mc.player.posZ - vec.zCoord) + (mc.player.posX - vec.xCoord) * (mc.player.posX - vec.xCoord));
        angles[1] = (float) (Math.atan2(heights, distance) / Math.PI * 180.0D);
        return angles;
    }

    public static boolean isLookingAtTarget(float yaw, float pitch, Entity entity, double range) {
        Vec3d src = mc.player.getPositionEyes(1.0F);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);

        RayTraceResult rayTraceResult = mc.world.rayTraceBlocks(src, dest, false, false, true);

        if (rayTraceResult == null) {
            return true;
        }

        return (entity.getEntityBoundingBox().expand(0.06, 0.06, 0.06).calculateIntercept(src, dest) == null);
    }

    public static boolean isLookingAt(Vec3d vec) {
        float[] angles = getLookAngles(vec);
        float change = Math.abs(MathHelper.wrapDegrees(angles[0] - Rotation.getFakeYaw())) / 0.6f;
        return (change < 20.0F);
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight(), mc.player.posZ);
    }

    public static float[] getRotations(Entity entityIn, boolean random) {

        double diffX = entityIn.posX - mc.player.posX;
        double diffZ = entityIn.posZ - mc.player.posZ;

        double diffY;
        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - 0.3;
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2 - (mc.player.posY + mc.player.getEyeHeight());
        }

        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float randomYaw = 0;
        if (random) {
            randomYaw = RandomUtils.nextFloat(-2, 2);
        }

        float randomPitch = 0;
        if (random) {
            randomPitch = RandomUtils.nextFloat(-2, 2);
        }

        float yaw = (float) ((Math.atan2(diffZ, diffX) * 180 / Math.PI) - 90.0F) + randomYaw;
        float pitch = (float) -(Math.atan2(diffY, diffXZ) * 180 / Math.PI) + randomPitch;
        pitch = (MathHelper.clamp(pitch, -90.0F, 90.0F));
        float mouseSens = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float gcd = mouseSens * mouseSens * mouseSens * 1.2F;
        yaw -= yaw % gcd;
        pitch -= pitch % gcd;

        return new float[]{yaw, pitch};
    }

    public static float updateRotation(float current, float intended, float speed) {
        float f = MathHelper.wrapDegrees(intended - current);
        if (f > speed)
            f = speed;
        if (f < -speed)
            f = -speed;
        return current + f;
    }

    public static class Rotation {

        public static boolean preready = false;
        public static float fakePitch;
        public static float fakeYaw;
        public static float lastfakepitch;
        public static float lastfakeyaw;
        public static float fakerenderyawoffset;
        public static float lastfakerenderyawoffset;
        public static float bodyYaw;
        public static float lastBodyYaw;
        public static int rotationTickCounter;
        private static boolean fakeaim;

        public static void updateAngle() {
            Minecraft mc = Minecraft.getMinecraft();
            double x = mc.player.posX - mc.player.prevPosX;
            double z = mc.player.posZ - mc.player.prevPosZ;
            if (x * x + z * z > 2.500000277905201E-7D) {
                bodyYaw = Clamp((float) MovementUtil.getPlayerMoveDir(), fakeYaw, 50.0F);
                rotationTickCounter = 0;
            } else if (rotationTickCounter > 0) {
                rotationTickCounter--;
                bodyYaw = checkAngle(fakeYaw, bodyYaw, 10.0F);
            }
        }

        public static float checkAngle(float oneVar, float twoVar, float threeVar) {
            float f = MathHelper.wrapDegrees(oneVar - twoVar);
            if (f < -threeVar) {
                f = -threeVar;
            }
            if (f >= threeVar) {
                f = threeVar;
            }
            return oneVar - f;
        }

        private static float Clamp(float bodyYaw2, float fakeYaw2, float f) {
            return 0;
        }

        public static float getFakeYaw() {
            return fakeYaw;
        }

        public static boolean isFakeAim() {
            return fakeaim;
        }


        @EventTarget
        public void onMotion(EventMotion e) {
            if (!isFakeAim() && e.isPre()) {
                preready = true;
            } else if (!isFakeAim() && preready && e.isPost()) {
                fakePitch = (Minecraft.getMinecraft()).player.rotationPitch;
                fakeYaw = (Minecraft.getMinecraft()).player.rotationYaw;
                lastfakepitch = (Minecraft.getMinecraft()).player.rotationPitch;
                lastfakeyaw = (Minecraft.getMinecraft()).player.rotationYaw;
                bodyYaw = (Minecraft.getMinecraft()).player.renderYawOffset;
                preready = false;
            } else {
                preready = false;
            }
            if (e.isPre()) {
                lastBodyYaw = bodyYaw;
                lastfakepitch = fakePitch;
                lastfakeyaw = fakeYaw;
                bodyYaw = Clamp(bodyYaw, fakeYaw, 50.0F);
                updateAngle();
                lastfakerenderyawoffset = fakerenderyawoffset;
                fakerenderyawoffset = fakeYaw;
            }
        }

        @EventTarget
        public void onPacket(EventReceivePacket e) {
            if (e.getPacket() instanceof CPacketAnimation) {
                rotationTickCounter = 10;
            }
        }

    }
}