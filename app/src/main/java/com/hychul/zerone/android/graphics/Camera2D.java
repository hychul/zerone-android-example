package com.hychul.zerone.android.graphics;

import android.opengl.GLES10;

import com.hychul.zerone.android.Graphics;
import com.hychul.zerone.math.Vector2;

import javax.microedition.khronos.opengles.GL10;

public class Camera2D {

    final Graphics graphics;

    public final Vector2 position;

    // TODO: Use screen resolution
    public final float frustumWidth;
    public final float frustumHeight;

    public float zoom;

    float near;
    float far;

    public Camera2D(Graphics graphics, float frustumWidth, float frustumHeight) {
        this.graphics = graphics;

        this.frustumWidth = frustumWidth;
        this.frustumHeight = frustumHeight;
        this.position = new Vector2(frustumWidth / 2, frustumHeight / 2);
        this.zoom = 1.0f;

        this.near = -1;
        this.near = 1;
    }

    public void setViewport() {
        GLES10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
        GLES10.glMatrixMode(GL10.GL_PROJECTION);
        GLES10.glLoadIdentity();
        GLES10.glOrthof(position.x - frustumWidth * zoom / 2,
                        position.x + frustumWidth * zoom/ 2,
                        position.y - frustumHeight * zoom / 2,
                        position.y + frustumHeight * zoom/ 2,
                        near, far);
        GLES10.glMatrixMode(GL10.GL_MODELVIEW);
        GLES10.glLoadIdentity();
    }

    public void touchToWorld(Vector2 touch) {
        touch.x = (touch.x / (float) graphics.getWidth()) * frustumWidth * zoom;
        touch.y = (1 - touch.y / (float) graphics.getHeight()) * frustumHeight * zoom;
        touch.add(position).sub(frustumWidth * zoom / 2, frustumHeight * zoom / 2);
    }
}
