package com.hychul.zerone.android.graphics.light;

import android.opengl.GLES10;

import javax.microedition.khronos.opengles.GL10;

public class AmbientLight {

    float[] color = {0.2f, 0.2f, 0.2f, 1};

    public void setColor(float r, float g, float b, float a) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }

    public void enable() {
        GLES10.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, color, 0);
    }
}
