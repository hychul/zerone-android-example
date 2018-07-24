package com.hychul.example;

import android.opengl.GLES10;

import com.hychul.zerone.Input.TouchEvent;
import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLGraphics;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.core.GameObject;
import com.hychul.zerone.core.Scene;
import com.hychul.zerone.android.graphics.Camera2D;
import com.hychul.zerone.android.graphics.Texture;
import com.hychul.zerone.android.graphics.Vertices;
import com.hychul.zerone.math.OverlapTester;
import com.hychul.zerone.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class TextureAtlasTest extends ZeroneActivity {

	public Scene getStartScene() {
		return new TextureAtlasScene(this);
	}

	class TextureAtlasScene extends Scene {
	    final int NUM_TARGETS = 20;
	    final float WORLD_WIDTH = 9.6f;
	    final float WORLD_HEIGHT = 4.8f;
	    GLGraphics glGraphics;        
	    Cannon cannon;
	    GameObject ball;
	    List<GameObject> targets;
	    SpatialHashGrid grid;
	    
	    Vertices cannonVertices;
	    Vertices ballVertices;
	    Vertices targetVertices;
	    
	    Vector2 touchPos = new Vector2();
	    Vector2 gravity = new Vector2(0,-10);
	    
	    Camera2D camera;
	    
	    Texture texture;

	    public TextureAtlasScene(Zerone zerone) {
	        super(zerone);
	        glGraphics = ((ZeroneActivity) zerone).getGLGraphics();
	        
	        cannon = new Cannon(0, 0, 1, 0.5f);
	        ball = new GameObject(0, 0, 0.2f, 0.2f);
	        targets = new ArrayList<GameObject>(NUM_TARGETS);
	        grid = new SpatialHashGrid(WORLD_WIDTH, WORLD_HEIGHT, 2.5f);
	        for (int i = 0; i < NUM_TARGETS; i++) {
	            GameObject target = new GameObject((float)Math.random() * WORLD_WIDTH, 
	                                               (float)Math.random() * WORLD_HEIGHT,
	                                               0.5f, 0.5f);  
	            grid.insertStaticObject(target);
	            targets.add(target);
	        }                        
	        
	        cannonVertices = new Vertices(4, 6, false, true);
	        cannonVertices.setVertices(new float[] { -0.5f, -0.25f, 0.0f, 0.0f, 0.5f,
	                                                  0.5f, -0.25f, 0.0f, 1.0f, 0.5f,
	                                                  0.5f,  0.25f, 0.0f, 1.0f, 0.0f,
	                                                 -0.5f,  0.25f, 0.0f, 0.0f, 0.0f },
	                                                 0, 20);
	        cannonVertices.setIndices(new short[] {0, 1, 2, 2, 3, 0}, 0, 6);

	        ballVertices = new Vertices(4, 6, false, true);
	        ballVertices.setVertices(new float[] { -0.1f, -0.1f, 0.0f, 0.0f, 0.75f,
	                                                0.1f, -0.1f, 0.0f, 0.25f, 0.75f,
	                                                0.1f,  0.1f, 0.0f, 0.25f, 0.5f,
	                                               -0.1f,  0.1f, 0.0f, 0.0f, 0.5f },
	                                                0, 20);
	        ballVertices.setIndices(new short[] {0, 1, 2, 2, 3, 0}, 0, 6);

	        targetVertices = new Vertices(4, 6, false, true);
	        targetVertices.setVertices(new float[] { -0.25f, -0.25f, 0.0f, 0.5f, 1.0f,
	                                                  0.25f, -0.25f, 0.0f, 1.0f, 1.0f,
	                                                  0.25f,  0.25f, 0.0f, 1.0f, 0.5f,
	                                                 -0.25f,  0.25f, 0.0f, 0.5f, 0.5f },
	                                                 0, 20);
	        targetVertices.setIndices(new short[] {0, 1, 2, 2, 3, 0}, 0, 6);
	        
	        camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
	    }
	    
	    @Override
	    public void update(float deltaTime) {
	        List<TouchEvent> touchEvents = zerone.getInput().getTouchEvents();
	        zerone.getInput().getKeyEvents();

	        int len = touchEvents.size();
	        for (int i = 0; i < len; i++) {
	            TouchEvent event = touchEvents.get(i);
	            
	            camera.touchToWorld(touchPos.set(event.x, event.y));

	            cannon.angle = touchPos.sub(cannon.position).angle();                       
	            
	            if (event.type == TouchEvent.TOUCH_UP) {
	                float radians = cannon.angle * Vector2.TO_RADIANS;
	                float ballSpeed = touchPos.len() * 2;
	                ball.position.set(cannon.position);
	                ball.velocity.x = (float)Math.cos(radians) * ballSpeed;
	                ball.velocity.y = (float)Math.sin(radians) * ballSpeed;
	                ball.bounds.lowerLeft.set(ball.position.x - 0.1f, ball.position.y - 0.1f);
	            }
	        }
	                                      
	        ball.velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
	        ball.position.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);
	        ball.bounds.lowerLeft.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);
	        
	        List<GameObject> colliders = grid.getPotentialColliders(ball);
	        len = colliders.size(); 
	        for (int i = 0; i < len; i++) {
	            GameObject collider = colliders.get(i);
	            if (OverlapTester.overlapRectangles(ball.bounds, collider.bounds)) {
	                grid.removeObject(collider);
	                targets.remove(collider);
	            }
	        }
	        
	        if (ball.position.y > 0) {
	            camera.position.set(ball.position);
	            camera.zoom = 1 + ball.position.y / WORLD_HEIGHT; 
	        } else {
	            camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
	            camera.zoom = 1;
	        }
	    }

	    @Override
	    public void render(float deltaTime) {
	        GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
	        camera.setViewportAndMatrices();

	        GLES10.glEnable(GL10.GL_BLEND);
	        GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	        GLES10.glEnable(GL10.GL_TEXTURE_2D);
	        texture.bind();

	        targetVertices.bind();
	        int len = targets.size();
	        for (int i = 0; i < len; i++) {
	            GameObject target = targets.get(i);
	            GLES10.glLoadIdentity();
	            GLES10.glTranslatef(target.position.x, target.position.y, 0);
	            targetVertices.draw(GL10.GL_TRIANGLES, 0, 6);
	        }
	        targetVertices.unbind();

	        GLES10.glLoadIdentity();
	        GLES10.glTranslatef(ball.position.x, ball.position.y, 0);
	        ballVertices.bind();
	        ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
	        ballVertices.unbind();  

	        GLES10.glLoadIdentity();
	        GLES10.glTranslatef(cannon.position.x, cannon.position.y, 0);
	        GLES10.glRotatef(cannon.angle, 0, 0, 1);
	        cannonVertices.bind();
	        cannonVertices.draw(GL10.GL_TRIANGLES, 0, 6);
	        cannonVertices.unbind();                    
	    }

		@Override
		public void onPause() {
		}

		@Override
		public void onResume() {
		    texture = new Texture(zerone.getFileIO(), "atlas.png");
		}

		@Override
		public void onDestroy() {
		}
	}
}
