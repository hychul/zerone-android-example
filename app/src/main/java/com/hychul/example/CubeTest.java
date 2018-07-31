package com.hychul.example;

import android.opengl.GLES10;
import android.opengl.GLU;

import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Texture;
import com.hychul.zerone.android.graphics.Vertices;
import com.hychul.zerone.core.Scene;
import com.hychul.zerone.core.Time;

import javax.microedition.khronos.opengles.GL10;

public class CubeTest extends ZeroneActivity {

    @Override
    public Scene getStartScene() {
        return new CubeScene(this);
    }

    class CubeScene extends GLScene {

        Vertices cube;
        Texture texture;
        float angle = 0;

        public CubeScene(ZeroneActivity zerone) {
            super(zerone);
        }

        @Override
        public void onCreate() {
            cube = createCube();
            texture = new Texture(zerone.getFileIO(), "crate.png");
        }

        private Vertices createCube() {
            float[] vertices = {-0.5f, -0.5f, 0.5f, 0, 1,
                                0.5f, -0.5f, 0.5f, 1, 1,
                                0.5f, 0.5f, 0.5f, 1, 0,
                                -0.5f, 0.5f, 0.5f, 0, 0,

                                0.5f, -0.5f, 0.5f, 0, 1,
                                0.5f, -0.5f, -0.5f, 1, 1,
                                0.5f, 0.5f, -0.5f, 1, 0,
                                0.5f, 0.5f, 0.5f, 0, 0,

                                0.5f, -0.5f, -0.5f, 0, 1,
                                -0.5f, -0.5f, -0.5f, 1, 1,
                                -0.5f, 0.5f, -0.5f, 1, 0,
                                0.5f, 0.5f, -0.5f, 0, 0,

                                -0.5f, -0.5f, -0.5f, 0, 1,
                                -0.5f, -0.5f, 0.5f, 1, 1,
                                -0.5f, 0.5f, 0.5f, 1, 0,
                                -0.5f, 0.5f, -0.5f, 0, 0,

                                -0.5f, 0.5f, 0.5f, 0, 1,
                                0.5f, 0.5f, 0.5f, 1, 1,
                                0.5f, 0.5f, -0.5f, 1, 0,
                                -0.5f, 0.5f, -0.5f, 0, 0,

                                -0.5f, -0.5f, 0.5f, 0, 1,
                                0.5f, -0.5f, 0.5f, 1, 1,
                                0.5f, -0.5f, -0.5f, 1, 0,
                                -0.5f, -0.5f, -0.5f, 0, 0
            };

            short[] indices = {0, 1, 3, 1, 2, 3,
                               4, 5, 7, 5, 6, 7,
                               8, 9, 11, 9, 10, 11,
                               12, 13, 15, 13, 14, 15,
                               16, 17, 19, 17, 18, 19,
                               20, 21, 23, 21, 22, 23,
                               };

            Vertices cube = new Vertices(24, 36, false, true, false);
            cube.setVertices(vertices, 0, vertices.length);
            cube.setIndices(indices, 0, indices.length);
            return cube;
        }

        @Override
        public void onResume() {
            texture.reload();
        }

        @Override
        public void update() {
            float deltaTime = Time.getDeltaTime();
            angle += 45 * deltaTime;
        }

        @Override
        public void draw() {
            GL10 gl = graphics.getGL();
            GLES10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            GLES10.glMatrixMode(GL10.GL_PROJECTION);
            GLES10.glLoadIdentity();
            GLU.gluPerspective(gl, 67,
                               graphics.getWidth() / (float) graphics.getHeight(),
                               0.1f, 10.0f);
            GLES10.glMatrixMode(GL10.GL_MODELVIEW);
            GLES10.glLoadIdentity();

            GLES10.glEnable(GL10.GL_DEPTH_TEST);
            GLES10.glEnable(GL10.GL_TEXTURE_2D);
            texture.bind();
            cube.bind();
            GLES10.glTranslatef(0, 0, -3);
            GLES10.glRotatef(angle, 0, 1, 0);
            cube.draw(GL10.GL_TRIANGLES, 0, 36);
            cube.unbind();
            GLES10.glDisable(GL10.GL_TEXTURE_2D);
            GLES10.glDisable(GL10.GL_DEPTH_TEST);
        }

        @Override
        public void onPause() {
        }

        @Override
        public void onDestroy() {
        }
    }
}
