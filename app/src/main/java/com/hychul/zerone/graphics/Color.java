package com.hychul.zerone.graphics;

public final class Color {

    public float r;
    public float g;
    public float b;
    public float a;

    public Color(Color color) {
        set(color);
    }

    public Color(float r, float g, float b, float a) {
        set(r, g, b, a);
    }

    public Color(String rgba) {
        r = Integer.parseInt(rgba.substring(0, 2), 16) / 256.0f;
        g = Integer.parseInt(rgba.substring(2, 4), 16) / 256.0f;
        b = Integer.parseInt(rgba.substring(4, 6), 16) / 256.0f;
        a = Integer.parseInt(rgba.substring(6, 8), 16) / 256.0f;
    }

    public void set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void set(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
    }

    public static Color black() {
        return new Color(0, 0, 0, 1);
    }

    public static Color white() {
        return new Color(1, 1, 1, 1);
    }
}