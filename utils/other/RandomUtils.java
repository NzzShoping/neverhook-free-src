package ru.neverhook.utils.other;

public class RandomUtils {

    public static float nextFloat(float startInclusive, float endInclusive) {
        if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0f) {
            return startInclusive;
        }
        return (float) (startInclusive + (endInclusive - startInclusive) * Math.random());
    }
}
