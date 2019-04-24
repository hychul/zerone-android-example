package com.zerone.example;

import android.opengl.GLES10;
import android.opengl.GLU;

import com.zerone.example.common.ActScene;
import com.zerone.android.ZeroneActivity;
import com.zerone.android.graphics.Vertices;
import com.zerone.core.Scene;

import javax.microedition.khronos.opengles.GL10;

public class ZBlendingTest extends ZeroneActivity {

    @Override
    public Scene getStartScene() {
        return new ZBlendingScene(this);
    }

    class ZBlendingScene extends ActScene {
        Vertices vertices;

        public ZBlendingScene(ZeroneActivity zerone) {
            super(zerone);
        }

        @Override
        public void onCreate() {
            vertices = new Vertices(6, 0, true, false, false);
            vertices.setVertices(new float[]{-0.5f, -0.5f, -3, 1, 0, 0, 0.5f,
                                             0.5f, -0.5f, -3, 1, 0, 0, 0.5f,
                                             0.0f, 0.5f, -3, 1, 0, 0, 0.5f,
                                             0.0f, -0.5f, -5, 0, 1, 0, 1,
                                             1.0f, -0.5f, -5, 0, 1, 0, 1,
                                             0.5f, 0.5f, -5, 0, 1, 0, 1}, 0, 7 * 6);
        }

        @Override
        public void draw() {
            GL10 gl = graphics.getGL();
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            GLES10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            GLES10.glMatrixMode(GL10.GL_PROJECTION);
            GLES10.glLoadIdentity();
            GLU.gluPerspective(gl, 67,
                               graphics.getWidth() / (float) graphics.getHeight(),
                               0.1f, 10f);
            GLES10.glMatrixMode(GL10.GL_MODELVIEW);
            GLES10.glLoadIdentity();

            GLES10.glEnable(GL10.GL_DEPTH_TEST);
            GLES10.glEnable(GL10.GL_BLEND);
            GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

            vertices.bind();
            vertices.draw(GL10.GL_TRIANGLES, 3, 3);
            vertices.draw(GL10.GL_TRIANGLES, 0, 3);
            vertices.unbind();

            GLES10.glDisable(GL10.GL_BLEND);
            GLES10.glDisable(GL10.GL_DEPTH_TEST);
        }

        @Override
        public void update() {
        }

        @Override
        public void onPause() {
        }

        @Override
        public void onResume() {
        }

        @Override
        public void onDestroy() {
        }
    }
}
