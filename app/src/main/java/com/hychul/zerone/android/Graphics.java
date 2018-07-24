package com.hychul.zerone.android;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;

public class Graphics {

    GLSurfaceView glView;
    GL10 gl;
    
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
        return glView.getWidth();
    }
    
    public int getHeight() {
        return glView.getHeight();
    }
}
