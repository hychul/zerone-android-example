package com.hychul.zerone.math;

public final class Mathf {

    public static final float E = 2.718281828459045f;
    public static final float PI = 3.141592653589793f;

    public static final float DEG_TO_RADIANS = PI / 180.0f;
    public static final float RAD_TO_DEGREES = 180.0f / PI;

    public static float sin(float var0) {
        return (float) Math.sin(var0);
    }

    public static float cos(float var0) {
        return (float) Math.cos(var0);
    }

    public static float tan(float var0) {
        return (float) Math.tan(var0);
    }

    public static float asin(float var0) {
        return (float) Math.asin(var0);
    }

    public static float acos(float var0) {
        return (float) Math.acos(var0);
    }

    public static float atan(float var0) {
        return (float) Math.atan(var0);
    }

    public static float toRadians(float angdeg) {
        return angdeg * DEG_TO_RADIANS;
    }

    public static float toDegrees(float angrad) {
        return angrad * RAD_TO_DEGREES;
    }

    public static float exp(float var0) {
        return (float) Math.exp(var0);
    }

    public static float log(float var0) {
        return (float) Math.log(var0);
    }

    public static float log10(float var0) {
        return (float) Math.log10(var0);
    }

    public static float sqrt(float var0) {
        return (float) Math.sqrt(var0);
    }

    public static float cbrt(float var0) {
        return (float) Math.cbrt(var0);
    }

    public static float IEEEremainder(float var0, float var2) {
        return (float) Math.IEEEremainder(var0, var2);
    }

    public static float ceil(float var0) {
        return (float) Math.ceil(var0);
    }

    public static float floor(float var0) {
        return (float) Math.floor(var0);
    }

    public static float rint(float var0) {
        return (float) Math.rint(var0);
    }

    public static float atan2(float var0, float var2) {
        return (float) Math.atan2(var0, var2);
    }

    public static float pow(float var0, float var2) {
        return (float) Math.pow(var0, var2);
    }

    public static int round(float a) {
        return Math.round(a);
    }

    public static float random() {
        return (float) Math.random();
    }

    public static float abs(float a) {
        return Math.abs(a);
    }

    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    public static float min(float a, float b) {
        return Math.min(a, b);
    }

    public static float ulp(float f) {
        return Math.ulp(f);
    }

    public static float signum(float f) {
        return Math.signum(f);
    }

    public static float sinh(float var0) {
        return (float) Math.sinh(var0);
    }

    public static float cosh(float var0) {
        return (float) Math.cosh(var0);
    }

    public static float tanh(float var0) {
        return (float) Math.tanh(var0);
    }

    public static float hypot(float var0, float var2) {
        return (float) Math.hypot(var0, var2);
    }

    public static float expm1(float var0) {
        return (float) Math.expm1(var0);
    }

    public static float log1p(float var0) {
        return (float) Math.log1p(var0);
    }

    public static float copySign(float magnitude, float sign) {
        return Math.copySign(magnitude, sign);
    }

    public static int getExponent(float f) {
        return Math.getExponent(f);
    }

    public static float scalb(float f, int scaleFactor) {
        return Math.scalb(f, scaleFactor);
    }
}
