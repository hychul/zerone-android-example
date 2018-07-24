package com.hychul.example.eulercamera;

import android.opengl.GLES10;
import android.opengl.GLU;
import android.opengl.Matrix;

import com.hychul.zerone.android.GLGraphics;
import com.hychul.zerone.math.Vector3;

import javax.microedition.khronos.opengles.GL10;

public class EulerCamera {
    final GLGraphics glGraphics;
    final Vector3 position = new Vector3();
    float yaw;
    float pitch;
    float fieldOfView;
    float aspectRatio;
    float near;
    float far;

    final float[] matrix = new float[16];
    final float[] inVec = { 0, 0, -1, 1 };
    final float[] outVec = new float[4];
    final Vector3 direction = new Vector3();

    public EulerCamera(GLGraphics glGraphics, float fieldOfView, float aspectRatio, float near, float far){
        this.glGraphics = glGraphics;
        this.fieldOfView = fieldOfView;
        this.aspectRatio = aspectRatio;
        this.near = near;
        this.far = far;
    }

    public Vector3 getPosition() {
        return position;
    }
    public float getYaw() {
        return yaw;
    }
    
    public float getPitch() {
        return pitch;
    }

    public void setAngles(float yaw, float pitch) {
        if (pitch < -90)
            pitch = -90;
        if (pitch > 90)
            pitch = 90;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void rotate(float yawInc, float pitchInc) {
        this.yaw += yawInc;
        this.pitch += pitchInc;
        if (pitch < -90)
            pitch = -90;
        if (pitch > 90)
            pitch = 90;
    }

    public void setMatrices() {
        GL10 gl = glGraphics.getGL();
        GLES10.glMatrixMode(GL10.GL_PROJECTION);
        GLES10.glLoadIdentity();
        GLU.gluPerspective(gl, fieldOfView, aspectRatio, near, far);
        GLES10.glMatrixMode(GL10.GL_MODELVIEW);
        GLES10.glLoadIdentity();
        GLES10.glRotatef(-pitch, 1, 0, 0);
        GLES10.glRotatef(-yaw, 0, 1, 0);
        GLES10.glTranslatef(-position.x, -position.y, -position.z);
    }

    public Vector3 getDirection() {
        Matrix.setIdentityM(matrix, 0);
        Matrix.rotateM(matrix, 0, yaw, 0, 1, 0);
        Matrix.rotateM(matrix, 0, pitch, 1, 0, 0);
        Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
        direction.set(outVec[0], outVec[1], outVec[2]);
        return direction;
    }
}