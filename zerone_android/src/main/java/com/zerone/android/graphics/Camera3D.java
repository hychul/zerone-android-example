package com.zerone.android.graphics;

import android.opengl.GLES10;
import android.opengl.GLU;

import com.zerone.android.Graphics;
import com.zerone.math.Vector3;

import javax.microedition.khronos.opengles.GL10;

public class Camera3D {

    final Graphics graphics;

    final Vector3 position;

    final Vector3 up;
    final Vector3 lookAt;

    float fieldOfView;
    float aspectRatio;

    float near;
    float far;

    public Camera3D(Graphics graphics, float fieldOfView, float near, float far) {
        this.graphics = graphics;

        this.position = new Vector3();
        this.up = new Vector3(0, 1, 0);
        this.lookAt = new Vector3(0, 0, -1);

        this.fieldOfView = fieldOfView;
        this.aspectRatio = (float) graphics.getWidth() / graphics.getHeight();

        this.near = near;
        this.far = far;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getUp() {
        return up;
    }

    public Vector3 getLookAt() {
        return lookAt;
    }

    public void setViewport() {
        GL10 gl = graphics.getGL();

        GLES10.glMatrixMode(GL10.GL_PROJECTION);
        GLES10.glLoadIdentity();
        GLU.gluPerspective(gl, fieldOfView, aspectRatio, near, far);
        GLES10.glMatrixMode(GL10.GL_MODELVIEW);
        GLES10.glLoadIdentity();
        GLU.gluLookAt(gl, position.x, position.y, position.z, lookAt.x, lookAt.y, lookAt.z, up.x, up.y, up.z);
    }
}
