package com.hychul.example;

import android.opengl.GLES10;

import com.hychul.zerone.Input.TouchEvent;
import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Vertices;
import com.hychul.zerone.core.Scene;
import com.hychul.zerone.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class CannonTest extends ZeroneActivity {
    public Scene getStartScene() {
        return new CannonScene(this);
    }

    class CannonScene extends GLScene {
        float FRUSTUM_WIDTH = 4.8f;
        float FRUSTUM_HEIGHT = 3.2f;
        Vertices vertices;
        Vector2 cannonPos = new Vector2(2.4f, 0.5f);
        float cannonAngle = 0;
        Vector2 touchPos = new Vector2();

        public CannonScene(Zerone zerone) {
            super(zerone);
            vertices = new Vertices(3, 0, false, false);
            vertices.setVertices(new float[]{-0.5f, -0.5f, 0.0f,
                                             0.5f, 0.0f, 0.0f,
                                             -0.5f, 0.5f, 0.0f}, 0, 6);
        }

        @Override
        public void update(float deltaTime) {
            List<TouchEvent> touchEvents = zerone.getInput().getTouchEvents();
            zerone.getInput().getKeyEvents();

            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);

                touchPos.x = (event.x / (float) graphics.getWidth())
                             * FRUSTUM_WIDTH;
                touchPos.y = (1 - event.y / (float) graphics.getHeight())
                             * FRUSTUM_HEIGHT;
                cannonAngle = touchPos.sub(cannonPos).angle();
            }
        }

        @Override
        public void draw() {
            GLES10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
            GLES10.glMatrixMode(GL10.GL_PROJECTION);
            GLES10.glLoadIdentity();
            GLES10.glOrthof(0, FRUSTUM_WIDTH, 0, FRUSTUM_HEIGHT, 1, -1);
            GLES10.glMatrixMode(GL10.GL_MODELVIEW);
            GLES10.glLoadIdentity();

            GLES10.glTranslatef(cannonPos.x, cannonPos.y, 0);
            GLES10.glRotatef(cannonAngle, 0, 0, 1);
            vertices.bind();
            vertices.draw(GL10.GL_TRIANGLES, 0, 3);
            vertices.unbind();
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
