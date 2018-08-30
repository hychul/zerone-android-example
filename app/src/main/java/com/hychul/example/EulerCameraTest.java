package com.hychul.example;

import android.opengl.GLES10;

import com.hychul.example.common.ActScene;
import com.hychul.example.common.EulerCamera;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Camera2D;
import com.hychul.zerone.android.graphics.Sprite;
import com.hychul.zerone.android.graphics.SpriteBatcher;
import com.hychul.zerone.android.graphics.Texture;
import com.hychul.zerone.android.graphics.Vertices;
import com.hychul.zerone.android.graphics.light.PointLight;
import com.zerone.core.Scene;
import com.zerone.core.Time;
import com.zerone.math.Vector2;
import com.zerone.math.Vector3;

import javax.microedition.khronos.opengles.GL10;

public class EulerCameraTest extends ZeroneActivity {

    @Override
    public Scene getStartScene() {
        return new EulerCameraScene(this);
    }

    class EulerCameraScene extends ActScene {

        Texture crateTexture;
        Vertices cube;
        PointLight light;
        EulerCamera camera;
        Texture buttonTexture;
        SpriteBatcher batcher;
        Camera2D guiCamera;
        Sprite buttonRegion;
        Vector2 touchPos;
        float lastX = -1;
        float lastY = -1;

        public EulerCameraScene(ZeroneActivity zerone) {
            super(zerone);
        }

        @Override
        public void onCreate() {
            crateTexture = new Texture(zerone.getFileIO(), "crate.png", true);
            cube = createCube();
            light = new PointLight();
            light.setPosition(3, 3, -3);
            camera = new EulerCamera(graphics, 67, graphics.getWidth() / (float) graphics.getHeight(), 1, 100);
            camera.getPosition().set(0, 1, 3);

            buttonTexture = new Texture(zerone.getFileIO(), "button.png");
            batcher = new SpriteBatcher(1);
            guiCamera = new Camera2D(graphics, 480, 320);
            buttonRegion = new Sprite(buttonTexture, 0, 0, 64, 64);
            touchPos = new Vector2();
        }

        private Vertices createCube() {
            float[] vertices = {-0.5f, -0.5f, 0.5f, 0, 1, 0, 0, 1,
                                0.5f, -0.5f, 0.5f, 1, 1, 0, 0, 1,
                                0.5f, 0.5f, 0.5f, 1, 0, 0, 0, 1,
                                -0.5f, 0.5f, 0.5f, 0, 0, 0, 0, 1,

                                0.5f, -0.5f, 0.5f, 0, 1, 1, 0, 0,
                                0.5f, -0.5f, -0.5f, 1, 1, 1, 0, 0,
                                0.5f, 0.5f, -0.5f, 1, 0, 1, 0, 0,
                                0.5f, 0.5f, 0.5f, 0, 0, 1, 0, 0,

                                0.5f, -0.5f, -0.5f, 0, 1, 0, 0, -1,
                                -0.5f, -0.5f, -0.5f, 1, 1, 0, 0, -1,
                                -0.5f, 0.5f, -0.5f, 1, 0, 0, 0, -1,
                                0.5f, 0.5f, -0.5f, 0, 0, 0, 0, -1,

                                -0.5f, -0.5f, -0.5f, 0, 1, -1, 0, 0,
                                -0.5f, -0.5f, 0.5f, 1, 1, -1, 0, 0,
                                -0.5f, 0.5f, 0.5f, 1, 0, -1, 0, 0,
                                -0.5f, 0.5f, -0.5f, 0, 0, -1, 0, 0,

                                -0.5f, 0.5f, 0.5f, 0, 1, 0, 1, 0,
                                0.5f, 0.5f, 0.5f, 1, 1, 0, 1, 0,
                                0.5f, 0.5f, -0.5f, 1, 0, 0, 1, 0,
                                -0.5f, 0.5f, -0.5f, 0, 0, 0, 1, 0,

                                -0.5f, -0.5f, -0.5f, 0, 1, 0, -1, 0,
                                0.5f, -0.5f, -0.5f, 1, 1, 0, -1, 0,
                                0.5f, -0.5f, 0.5f, 1, 0, 0, -1, 0,
                                -0.5f, -0.5f, 0.5f, 0, 0, 0, -1, 0};
            short[] indices = {0, 1, 2, 2, 3, 0,
                               4, 5, 6, 6, 7, 4,
                               8, 9, 10, 10, 11, 8,
                               12, 13, 14, 14, 15, 12,
                               16, 17, 18, 18, 19, 16,
                               20, 21, 22, 22, 23, 20,
                               24, 25, 26, 26, 27, 24};
            Vertices cube = new Vertices(vertices.length / 8, indices.length, false, true, true);
            cube.setVertices(vertices, 0, vertices.length);
            cube.setIndices(indices, 0, indices.length);
            return cube;
        }

        @Override
        public void onResume() {
            crateTexture.reload();
        }

        @Override
        public void update() {
            float deltaTime = Time.getDeltaTime();
            zerone.getInput().getTouchEvents();
            float x = zerone.getInput().getTouchX(0);
            float y = zerone.getInput().getTouchY(0);
            guiCamera.touchToWorld(touchPos.set(x, y));


            if (zerone.getInput().isTouchDown(0)) {
                if (touchPos.x < 64 && touchPos.y < 64) {
                    Vector3 direction = camera.getDirection();
                    camera.getPosition().add(direction.mul(deltaTime));
                } else {
                    if (lastX == -1) {
                        lastX = x;
                        lastY = y;
                    } else {
                        camera.rotate((x - lastX) / 10, (y - lastY) / 10);
                        lastX = x;
                        lastY = y;
                    }
                }
            } else {
                lastX = -1;
                lastY = -1;
            }
        }

        @Override
        public void draw() {
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            GLES10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());

            camera.setMatrices();

            GLES10.glEnable(GL10.GL_DEPTH_TEST);
            GLES10.glEnable(GL10.GL_TEXTURE_2D);
            GLES10.glEnable(GL10.GL_LIGHTING);

            crateTexture.bind();
            cube.bind();
            light.enable(GL10.GL_LIGHT0);

            for (int z = 0; z >= -8; z -= 2) {
                for (int x = -4; x <= 4; x += 2) {
                    GLES10.glPushMatrix();
                    GLES10.glTranslatef(x, 0, z);
                    cube.draw(GL10.GL_TRIANGLES, 0, 6 * 2 * 3);
                    GLES10.glPopMatrix();
                }
            }

            cube.unbind();

            GLES10.glDisable(GL10.GL_LIGHTING);
            GLES10.glDisable(GL10.GL_DEPTH_TEST);

            GLES10.glEnable(GL10.GL_BLEND);
            GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

            guiCamera.setViewport();
            batcher.beginBatch(buttonTexture);
            batcher.drawSprite(buttonRegion, 64, 64, 32, 32, 0, 0);
            batcher.endBatch();

            GLES10.glDisable(GL10.GL_BLEND);
            GLES10.glDisable(GL10.GL_TEXTURE_2D);
        }

        @Override
        public void onPause() {

        }

        @Override
        public void onDestroy() {
        }
    }
}
