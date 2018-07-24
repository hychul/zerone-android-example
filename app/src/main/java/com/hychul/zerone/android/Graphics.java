package com.hychul.zerone.android;

import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

import javax.microedition.khronos.opengles.GL10;

public class Graphics {

    GLSurfaceView glView;
    GL10 gl;

    int width;
    int height;
    
    Graphics(GLSurfaceView glView) {
        this.glView = glView;
    }
    
    public GL10 getGL() {
        return gl;
    }
    
    void setGL(GL10 gl) {
        this.gl = gl;
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
