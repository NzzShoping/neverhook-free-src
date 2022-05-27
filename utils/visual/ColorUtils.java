package ru.neverhook.utils.visual;

import ru.neverhook.feature.hud.ArreyList;
import ru.neverhook.utils.other.MathUtils;

import java.awt.*;

public class ColorUtils {

    public static int getColor(Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static Color getColorWithOpacity(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static int getColor(int red, int green, int blue) {
        return getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        color |= blue;
        return color;
    }

    public static int getColor(int brightness) {
        return getColor(brightness, brightness, brightness, 255);
    }

    public static int getColor(int brightness, int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }

    public static Color TwoColoreffect(Color cl1, Color cl2, double speed) {
        double thing = (speed / 4 % 1);

        float val = MathUtils.clamp(((float) Math.sin((Math.PI * 6) * thing) / 2F + .5F), 0, 1);

        return new Color(MathUtils.lerp(cl1.getRed() / 255F, cl2.getRed() / 255F, val), MathUtils.lerp(cl1.getGreen() / 255F, cl2.getGreen() / 255F, val), MathUtils.lerp(cl1.getBlue() / 255F, cl2.getBlue() / 255F, val));
    }

    public static int astolfo(int yOffset, int yTotal) {
        float speed = ArreyList.time.getValFloat() * 1000;
        float hue = (System.currentTimeMillis() % (int) speed) + ((yOffset - yTotal) * 9L);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, 0.6F, 1F);
    }

    public static Color astolfoColors1(int yOffset, int yTotal) {
        float speed = 2900F;
        float hue = (float) (System.currentTimeMillis() % (int) speed) + ((yTotal - yOffset) * 9);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return new Color(hue, 0.5f, 1F);
    }

    public static Color rainbowCol(int delay, float saturation, float brightness) {
        double rainbow = Math.ceil((System.currentTimeMillis() + delay) / 16);
        rainbow %= 360.0D;
        return Color.getHSBColor((float) (rainbow / 360.0D), saturation, brightness);
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset) {
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;
        }
        double inverse_percent = 1 - offset;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static int rainbowNew(int delay, float saturation, float brightness) {
        double rainbow = Math.ceil((System.currentTimeMillis() + delay) / 16);
        rainbow %= 360.0D;
        return Color.getHSBColor((float) (rainbow / 360.0D), saturation, brightness).getRGB();
    }
}
