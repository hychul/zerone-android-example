package com.hychul.example;

import android.opengl.GLES10;
import android.opengl.GLU;

import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.core.Scene;
import com.hychul.zerone.android.graphics.Material;
import com.hychul.zerone.android.graphics.Texture;
import com.hychul.zerone.android.graphics.Vertices3;
import com.hychul.zerone.android.graphics.light.AmbientLight;
import com.hychul.zerone.android.graphics.light.DirectionalLight;
import com.hychul.zerone.android.graphics.light.PointLight;

import javax.microedition.khronos.opengles.GL10;

public class LightTest extends ZeroneActivity {
	public Scene getStartScene() {
		return new LightScene(this);
	}
	
	class LightScene extends GLScene {
	    float angle;
	    Vertices3 cube;
	    Texture texture;
	    AmbientLight ambientLight;
	    PointLight pointLight;
	    DirectionalLight directionalLight;
	    Material material;

	    public LightScene(Zerone zerone) {
	        super(zerone);

	        cube = createCube();
	        texture = new Texture(zeroneActivity.getFileIO(), "crate.png");
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

	    private Vertices3 createCube() {
	        float[] vertices = { -0.5f, -0.5f, 0.5f, 0, 1, 0, 0, 1,
	                              0.5f, -0.5f, 0.5f, 1, 1, 0, 0, 1,
	                              0.5f,  0.5f, 0.5f, 1, 0, 0, 0, 1,
	                             -0.5f,  0.5f, 0.5f, 0, 0, 0, 0, 1,
	                             
	                              0.5f, -0.5f,  0.5f, 0, 1, 1, 0, 0,
	                              0.5f, -0.5f, -0.5f, 1, 1, 1, 0, 0,
	                              0.5f,  0.5f, -0.5f, 1, 0, 1, 0, 0,
	                              0.5f,  0.5f,  0.5f, 0, 0, 1, 0, 0,
	                              
	                              0.5f, -0.5f, -0.5f, 0, 1, 0, 0, -1,
	                             -0.5f, -0.5f, -0.5f, 1, 1, 0, 0, -1,
	                             -0.5f,  0.5f, -0.5f, 1, 0, 0, 0, -1,
	                              0.5f,  0.5f, -0.5f, 0, 0, 0, 0, -1,
	                             
	                             -0.5f, -0.5f, -0.5f, 0, 1, -1, 0, 0,
	                             -0.5f, -0.5f,  0.5f, 1, 1, -1, 0, 0,
	                             -0.5f,  0.5f,  0.5f, 1, 0, -1, 0, 0,
	                             -0.5f,  0.5f, -0.5f, 0, 0, -1, 0, 0,
	                             
	                             -0.5f,  0.5f,  0.5f, 0, 1, 0, 1, 0,
	                              0.5f,  0.5f,  0.5f, 1, 1, 0, 1, 0,
	                              0.5f,  0.5f, -0.5f, 1, 0, 0, 1, 0,
	                             -0.5f,  0.5f, -0.5f, 0, 0, 0, 1, 0,
	                             
	                             -0.5f, -0.5f, -0.5f, 0, 1, 0, -1, 0,
	                              0.5f, -0.5f, -0.5f, 1, 1, 0, -1, 0,
	                              0.5f, -0.5f,  0.5f, 1, 0, 0, -1, 0,
	                             -0.5f, -0.5f,  0.5f, 0, 0, 0, -1, 0 };
	        short[] indices = { 0, 1, 2, 2, 3, 0,
	                            4, 5, 6, 6, 7, 4,
	                            8, 9, 10, 10, 11, 8,
	                            12, 13, 14, 14, 15, 12,
	                            16, 17, 18, 18, 19, 16,
	                            20, 21, 22, 22, 23, 20,
	                            24, 25, 26, 26, 27, 24 };
	        Vertices3 cube = new Vertices3(vertices.length / 8, indices.length, false, true, true);
	        cube.setVertices(vertices, 0, vertices.length);
	        cube.setIndices(indices, 0, indices.length);
	        return cube;
	    }

	    @Override
	    public void update(float deltaTime) {
	        angle += deltaTime * 20;
	    }

	    @Override
	    public void render(float deltaTime) {
	        GL10 gl = glGraphics.getGL();
	        GLES10.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
	        GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	        GLES10.glEnable(GL10.GL_DEPTH_TEST);
	        GLES10.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());

	        GLES10.glMatrixMode(GL10.GL_PROJECTION);
	        GLES10.glLoadIdentity();
	        GLU.gluPerspective(gl, 67, glGraphics.getWidth()
	                / (float) glGraphics.getHeight(), 0.1f, 10f);
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
