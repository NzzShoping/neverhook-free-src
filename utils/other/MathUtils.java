package ru.neverhook.utils.other;

import java.math.BigDecimal;
import java.util.Random;

public class MathUtils {
    private static final Random random = new Random();

    public static BigDecimal round(float f, int times) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(times, 4);
        return bd;
    }

    public static int getRandomInRange(int max, int min) {
        return (int) (min + (max - min) * random.nextDouble());
    }

    public static int randomize(int max, int min) {
        return -min + (int) (Math.random() * ((max - (-min)) + 1));
    }

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }
}