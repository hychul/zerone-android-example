package com.zerone.example;

import android.opengl.GLES10;
import android.opengl.GLU;

import com.zerone.example.common.ActScene;
import com.zerone.android.ZeroneActivity;
import com.zerone.android.graphics.Material;
import com.zerone.android.graphics.ObjLoader;
import com.zerone.android.graphics.Texture;
import com.zerone.android.graphics.Vertices;
import com.zerone.android.graphics.light.AmbientLight;
import com.zerone.android.graphics.light.DirectionalLight;
import com.zerone.android.graphics.light.PointLight;
import com.zerone.core.Scene;
import com.zerone.core.Time;

import javax.microedition.khronos.opengles.GL10;

public class ObjTest extends ZeroneActivity {

    @Override
    public Scene getStartScene() {
        return new ObjScene(this);
    }

    class ObjScene extends ActScene {

        float angle;
        Vertices cube;
        Texture texture;
        AmbientLight ambientLight;
        PointLight pointLight;
        DirectionalLight directionalLight;
        Material material;

        public ObjScene(ZeroneActivity zerone) {
            super(zerone);
        }

        @Override
        public void onCreate() {
            cube = ObjLoader.load(zerone.getFileIO(), "cube.obj");
            texture = new Texture(zerone.getFileIO(), "crate.png");
            ambientLight = new AmbientLight();
            ambientLight.setColor(0, 0.2f, 0, 1);
            pointLight = new PointLight();
            pointLight.setDiffuse(1, 0, 0, 1);
            pointLight.setPosition(3, 3, 0);
            directionalLight = new DirectionalLight();
            directionalLight.setDiffuse(0, 0, 1, 1);
            directionalLight.setDirection(1, 0, 0);
            material = new Material();
        }

        @Override
        public void onResume() {
            texture.reload();
        }

        @Override
        public void update() {
            float deltaTime = Time.Companion.getDeltaTime();
            angle += deltaTime * 20;
        }

        @Override
        public void draw() {
            GL10 gl = graphics.getGL();

            GLES10.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            GLES10.glEnable(GL10.GL_DEPTH_TEST);
            GLES10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());

            GLES10.glMatrixMode(GL10.GL_PROJECTION);
            GLES10.glLoadIdentity();
            GLU.gluPerspective(gl, 67, graphics.getWidth()
                                       / (float) graphics.getHeight(), 0.1f, 10f);
            GLES10.glMatrixMode(GL10.GL_MODELVIEW);
            GLES10.glLoadIdentity();
            GLU.gluLookAt(gl, 0, 1, 3, 0, 0, 0, 0, 1, 0);

            GLES10.glEnable(GL10.GL_LIGHTING);

            ambientLight.enable();
            pointLight.enable(GL10.GL_LIGHT0);
            directionalLight.enable(GL10.GL_LIGHT1);
            material.enable();

            GLES10.glEnable(GL10.GL_TEXTURE_2D);
            texture.bind();

            GLES10.glRotatef(angle, 0, 1, 0);
            cube.bind();
            cube.draw(GL10.GL_TRIANGLES, 0, 6 * 2 * 3);
            cube.unbind();

            pointLight.disable();
            directionalLight.disable();

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
