package com.zerone.math;

public class AxisAngle {

    public float angle;
    public float x;
    public float y;
    public float z;

    public AxisAngle() {
        this(0, 0, 0, 0);
    }

    public AxisAngle(AxisAngle axisAngle) {
        this(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }

    public AxisAngle(float angle, float x, float y, float z) {
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setZero() {
        set(0, 0, 0, 0);
    }

    public void set(AxisAngle axisAngle) {
        set(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }

    public void set(float angle, float x, float y, float z) {
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
