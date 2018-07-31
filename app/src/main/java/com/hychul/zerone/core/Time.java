package com.hychul.zerone.core;

public class Time {

    private static float deltaTime;

    static void setDeltaTime(float deltaTime) {
        Time.deltaTime = deltaTime;
    }

    public static float getDeltaTime() {
        return deltaTime;
    }
}
