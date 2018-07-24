package com.hychul.example;

import android.opengl.GLES10;

import com.hychul.example.eulercamera.EulerCamera;
import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.core.Scene;
import com.hychul.zerone.android.graphics.Camera2D;
import com.hychul.zerone.android.graphics.Sprite;
import com.hychul.zerone.android.graphics.SpriteBatcher;
import com.hychul.zerone.android.graphics.Texture;
import com.hychul.zerone.android.graphics.Vertices3;
import com.hychul.zerone.android.graphics.light.PointLight;
import com.hychul.zerone.math.Vector2;
import com.hychul.zerone.math.Vector3;

import javax.microedition.khronos.opengles.GL10;

public class EulerCameraTest extends ZeroneActivity {

	public Scene getStartScene() {
		return new EulerCameraScene(this);
	}
	
	class EulerCameraScene extends GLScene {
	    Texture crateTexture;
	    Vertices3 cube;
	    PointLight light;
	    EulerCamera camera;
	    Texture buttonTexture;
	    SpriteBatcher batcher;
	    Camera2D guiCamera;
	    Sprite buttonRegion;
	    Vector2 touchPos;
	    float lastX = -1;
	    float lastY = -1;

	    public EulerCameraScene(Zerone zerone) {
	        super(zerone);
	        
	        crateTexture = new Texture(zeroneActivity.getFileIO(), "crate.png", true);
	        cube = createCube();
	        light = new PointLight();
	        light.setPosition(3, 3, -3);            
	        camera = new EulerCamera(glGraphics, 67, glGraphics.getWidth() / (float)glGraphics.getHeight(), 1, 100);
	        camera.getPosition().set(0, 1, 3);
	        
	        buttonTexture = new Texture(zeroneActivity.getFileIO(), "button.png");
	        batcher = new SpriteBatcher(1);
	        guiCamera = new Camera2D(glGraphics, 480, 320);
	        buttonRegion = new Sprite(buttonTexture, 0, 0, 64, 64);
	        touchPos = new Vector2();
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
	     public void onResume() {
	         crateTexture.reload();
	     }

	     @Override
	     public void update(float deltaTime) {
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
	     public void render(float deltaTime) {
	         GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	         GLES10.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
	         
	         camera.setMatrices();
	         
	         GLES10.glEnable(GL10.GL_DEPTH_TEST);
	         GLES10.glEnable(GL10.GL_TEXTURE_2D);
	         GLES10.glEnable(GL10.GL_LIGHTING);
	         
	         crateTexture.bind();
	         cube.bind();        
	         light.enable(GL10.GL_LIGHT0);
	         
	         for (int z = 0; z >= -8; z-=2) {
	             for (int x = -4; x <=4; x+=2 ) {
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
	         
	         guiCamera.setViewportAndMatrices();
	         batcher.beginBatch(buttonTexture);
	         batcher.drawSprite(32, 32, 64, 64, buttonRegion);
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
