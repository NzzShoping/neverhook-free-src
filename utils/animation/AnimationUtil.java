package ru.neverhook.utils.animation;

import net.minecraft.client.Minecraft;

public class AnimationUtil {

    public static float animation(float animation, float target, float speedTarget) {
        float da = (target - animation) / Math.max((float) Minecraft.getDebugFPS(), 5) * 15;

        if (da > 0) {
            da = Math.max(speedTarget, da);
            da = Math.min(target - animation, da);
        } else if (da < 0) {
            da = Math.min(-speedTarget, da);
            da = Math.max(target - animation, da);
        }

        return animation + da;
    }

    public static double animate(double target, double current, double speed) {
        boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor <= 0.1) {
            factor = 0.1;
        }
        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }
        return current;
    }

    public static float calculateCompensation(float target, float current, long delta, double speed) {
        float diff = current - target;
        if (delta < 1) {
            delta = 1;
        }
        if (delta > 1000) {
            delta = 16;
        }
        if (diff > speed) {
            double xD = (Math.max(speed * delta / (1000 / 60), 0.5));
            current -= xD;
            if (current < target) {
                current = target;
            }
        } else if (diff < -speed) {
            double xD = (Math.max(speed * delta / (1000 / 60), 0.5));
            current += xD;
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }
}
