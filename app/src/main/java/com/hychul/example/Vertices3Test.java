package com.hychul.example;

import android.opengl.GLES10;

import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Vertices;
import com.hychul.zerone.core.Scene;

import javax.microedition.khronos.opengles.GL10;

public class Vertices3Test extends ZeroneActivity {

    public Scene getStartScene() {
        return new Vertices3Scene(this);
    }

    class Vertices3Scene extends GLScene {
        Vertices vertices;

        public Vertices3Scene(Zerone zerone) {
            super(zerone);

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
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
            GLES10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            GLES10.glMatrixMode(GL10.GL_PROJECTION);
            GLES10.glLoadIdentity();
            GLES10.glOrthof(-1, 1, -1, 1, 10, -10);
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
