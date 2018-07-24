package com.hychul.zerone.android.graphics;

import android.opengl.GLES10;

import com.hychul.zerone.android.GLGraphics;
import com.hychul.zerone.math.Vector2;

import javax.microedition.khronos.opengles.GL10;

public class Camera2D {
    public final Vector2 position;
    public float zoom;
    public final float frustumWidth;
    public final float frustumHeight;
    final GLGraphics glGraphics;

    public Camera2D(GLGraphics glGraphics, float frustumWidth, float frustumHeight) {
        this.glGraphics = glGraphics;
        this.frustumWidth = frustumWidth;
        this.frustumHeight = frustumHeight;
        this.position = new Vector2(frustumWidth / 2, frustumHeight / 2);
        this.zoom = 1.0f;
    }

    public void setViewportAndMatrices() {
        GLES10.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
        GLES10.glMatrixMode(GL10.GL_PROJECTION);
        GLES10.glLoadIdentity();
        GLES10.glOrthof(position.x - frustumWidth * zoom / 2,
                    position.x + frustumWidth * zoom/ 2, 
                    position.y - frustumHeight * zoom / 2, 
                    position.y + frustumHeight * zoom/ 2, 
                    1, -1);
        GLES10.glMatrixMode(GL10.GL_MODELVIEW);
        GLES10.glLoadIdentity();
    }

    public void touchToWorld(Vector2 touch) {
        touch.x = (touch.x / (float) glGraphics.getWidth()) * frustumWidth * zoom;
        touch.y = (1 - touch.y / (float) glGraphics.getHeight()) * frustumHeight * zoom;
        touch.add(position).sub(frustumWidth * zoom / 2, frustumHeight * zoom / 2);
    }
}
