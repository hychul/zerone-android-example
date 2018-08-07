package com.hychul.zerone.android;

import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

import javax.microedition.khronos.opengles.GL10;

public class Graphics {

    GLSurfaceView glView;
    GL10 gl10;

    int width;
    int height;

    Graphics(GLSurfaceView glView, GL10 gl10) {
        this.glView = glView;
        this.gl10 = gl10;
    }

    public GL10 getGL() {
        return gl10;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setResolution(int width, int height) {
        SurfaceHolder holder = glView.getHolder();
        holder.setFixedSize(width, height);
    }
}
