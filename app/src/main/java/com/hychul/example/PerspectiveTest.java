package com.hychul.example;

import android.opengl.GLES10;
import android.opengl.GLU;

import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.core.Scene;
import com.hychul.zerone.android.graphics.Vertices3;

import javax.microedition.khronos.opengles.GL10;

public class PerspectiveTest extends ZeroneActivity {

    public Scene getStartScene() {
        return new PerspectiveScene(this);
    }
    
    class PerspectiveScene extends GLScene {
        Vertices3 vertices;
        
        public PerspectiveScene(Zerone zerone) {
            super(zerone);
            
            vertices = new Vertices3(6, 0, true, false, false);
            vertices.setVertices(new float[] { -0.5f, -0.5f, -3, 1, 0, 0, 1,
                                                0.5f, -0.5f, -3, 1, 0, 0, 1,
                                                0.0f,  0.5f, -3, 1, 0, 0, 1,
                                                
                                                0.0f,  -0.5f, -5, 0, 1, 0, 1,
                                                1.0f,  -0.5f, -5, 0, 1, 0, 1,
                                                0.5f,  0.5f, -5, 0, 1, 0, 1}, 0, 7 * 6);
        }        

        @Override
        public void render() {
        	GL10 gl = glGraphics.getGL();
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
            GLES10.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
            GLES10.glMatrixMode(GL10.GL_PROJECTION);
            GLES10.glLoadIdentity();
            GLU.gluPerspective(gl, 67, 
                               glGraphics.getWidth() / (float)glGraphics.getHeight(), 
                               0.1f, 10f);
            GLES10.glMatrixMode(GL10.GL_MODELVIEW);
            GLES10.glLoadIdentity();
            vertices.bind();
            vertices.draw(GL10.GL_TRIANGLES, 0, 6);
            vertices.unbind();

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
