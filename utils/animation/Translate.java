package ru.neverhook.utils.animation;

import net.minecraft.client.Minecraft;

public class Translate {

    private double x;
    private double y;

    public Translate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void interpolate(double targetX, double targetY, double smoothing) {
        this.x = AnimationUtil.animate(targetX, this.x, smoothing);
        this.y = AnimationUtil.animate(targetY, this.y, smoothing);
    }

    public void interpolateNew(double targetX, double targetY, double smoothing) {
        this.x = AnimationUtil.calculateCompensation((float) targetX, (float) this.x, (long) smoothing, (float) smoothing);
        this.y = AnimationUtil.calculateCompensation((float) targetY, (float) this.y, (long) smoothing, (float) smoothing);
    }

    public void arrayListAnim(float targetX, float targetY, float xSpeed, float ySpeed) {
        int deltaX = (int) (Math.abs(targetX - x) * xSpeed);
        int deltaY = (int) (Math.abs(targetY - y) * ySpeed);
        x = AnimationUtil.calculateCompensation(targetX, (float) this.x, (long) Minecraft.frameTime, deltaX);
        y = AnimationUtil.calculateCompensation(targetY, (float) this.y, (long) Minecraft.frameTime, deltaY);
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
