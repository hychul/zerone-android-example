package com.hychul.example;

import android.opengl.GLES10;
import android.opengl.GLU;

import com.hychul.example.common.ActScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Vertices;
import com.hychul.zerone.core.Scene;

import javax.microedition.khronos.opengles.GL10;

public class PerspectiveTest extends ZeroneActivity {

    @Override
    public Scene getStartScene() {
        return new PerspectiveScene(this);
    }

    class PerspectiveScene extends ActScene {
        Vertices vertices;

        public PerspectiveScene(ZeroneActivity zerone) {
            super(zerone);
        }

        @Override
        public void onCreate() {
            vertices = new Vertices(6, 0, true, false, false);
            vertices.setVertices(new float[]{-0.5f, -0.5f, -3, 1, 0, 0, 1,
                                             0.5f, -0.5f, -3, 1, 0, 0, 1,
                                             0.0f, 0.5f, -3, 1, 0, 0, 1,

                                             0.0f, -0.5f, -5, 0, 1, 0, 1,
                                             1.0f, -0.5f, -5, 0, 1, 0, 1,
                                             0.5f, 0.5f, -5, 0, 1, 0, 1}, 0, 7 * 6);
        }

        @Override
        public void draw() {
            GL10 gl = graphics.getGL();
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
            GLES10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            GLES10.glMatrixMode(GL10.GL_PROJECTION);
            GLES10.glLoadIdentity();
            GLU.gluPerspective(gl, 67,
                               graphics.getWidth() / (float) graphics.getHeight(),
                               0.1f, 10f);
            GLES10.glMatrixMode(GL10.GL_MODELVIEW);
            GLES10.glLoadIdentity();
            vertices.bind();
            vertices.draw(GL10.GL_TRIANGLES, 0, 6);
            vertices.unbind();

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
