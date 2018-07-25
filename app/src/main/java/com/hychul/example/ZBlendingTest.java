package com.hychul.example;

import android.opengl.GLES10;
import android.opengl.GLU;

import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Vertices;
import com.hychul.zerone.core.Scene;

import javax.microedition.khronos.opengles.GL10;

public class ZBlendingTest extends ZeroneActivity {

    public Scene getStartScene() {
        return new ZBlendingScene(this);
    }

    class ZBlendingScene extends GLScene {
        Vertices vertices;

        public ZBlendingScene(Zerone zerone) {
            super(zerone);

            vertices = new Vertices(6, 0, true, false, false);
            vertices.setVertices(new float[]{-0.5f, -0.5f, -3, 1, 0, 0, 0.5f,
                                             0.5f, -0.5f, -3, 1, 0, 0, 0.5f,
                                             0.0f, 0.5f, -3, 1, 0, 0, 0.5f,
                                             0.0f, -0.5f, -5, 0, 1, 0, 1,
                                             1.0f, -0.5f, -5, 0, 1, 0, 1,
                                             0.5f, 0.5f, -5, 0, 1, 0, 1}, 0, 7 * 6);
        }

        @Override
        public void render() {
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
        public void update(float deltaTime) {
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
