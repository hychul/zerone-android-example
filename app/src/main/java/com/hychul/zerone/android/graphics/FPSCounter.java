package com.hychul.zerone.android.graphics;

import android.util.Log;

public class FPSCounter {

    long startTime = System.nanoTime();
    int frames = 0;

    public void logFrame() {
        frames++;
        if (1000000000 <= System.nanoTime() - startTime) {
            Log.d("FPSCounter", "fps: " + frames);
            frames = 0;
            startTime = System.nanoTime();
        }
    }
} 
