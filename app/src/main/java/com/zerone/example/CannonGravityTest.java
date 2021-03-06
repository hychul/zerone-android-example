package com.zerone.example;

import android.opengl.GLES10;

import com.zerone.example.common.ActScene;
import com.zerone.android.ZeroneActivity;
import com.zerone.android.graphics.Vertices;
import com.zerone.android.input.TouchEvent;
import com.zerone.core.Scene;
import com.zerone.core.Time;
import com.zerone.math.Mathf;
import com.zerone.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class CannonGravityTest extends ZeroneActivity {

    @Override
    public Scene getStartScene() {
        return new CannonGravityScene(this);
    }

    class CannonGravityScene extends ActScene {

        float FRUSTUM_WIDTH = 9.6f;
        float FRUSTUM_HEIGHT = 6.4f;

        Vertices cannonVertices;
        Vertices ballVertices;
        Vector2 cannonPos = new Vector2();
        float cannonAngle = 0;
        Vector2 touchPos = new Vector2();
        Vector2 ballPos = new Vector2(0, 0);
        Vector2 ballVelocity = new Vector2(0, 0);
        Vector2 gravity = new Vector2(0, -10);

        public CannonGravityScene(ZeroneActivity zerone) {
            super(zerone);
        }

        @Override
        public void onCreate() {
            cannonVertices = new Vertices(3, 0, false, false);
            cannonVertices.setVertices(new float[]{-0.5f, -0.5f, 0.0f,
                                                   0.5f, 0.0f, 0.0f,
                                                   -0.5f, 0.5f, 0.0f}, 0, 6);
            ballVertices = new Vertices(4, 6, false, false);
            ballVertices.setVertices(new float[]{-0.1f, -0.1f, 0.0f,
                                                 0.1f, -0.1f, 0.0f,
                                                 0.1f, 0.1f, 0.0f,
                                                 -0.1f, 0.1f, 0.0f}, 0, 8);
            ballVertices.setIndices(new short[]{0, 1, 2, 2, 3, 0}, 0, 6);
        }

        @Override
        public void update() {
            float deltaTime = Time.Companion.getDeltaTime();
            List<TouchEvent> touchEvents = zerone.getInput().getTouchEvents();
            zerone.getInput().getKeyEvents();

            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);

                touchPos.setX((event.x / (float) graphics.getWidth())
                        * FRUSTUM_WIDTH);
                touchPos.setY((1 - event.y / (float) graphics.getHeight())
                        * FRUSTUM_HEIGHT);
                cannonAngle = touchPos.sub(cannonPos).angle();

                if (event.phase == TouchEvent.TOUCH_UP) {
                    float radians = Mathf.toRadians(cannonAngle);
                    float ballSpeed = touchPos.len();
                    ballPos.set(cannonPos);
                    ballVelocity.setX(Mathf.cos(radians) * ballSpeed);
                    ballVelocity.setY(Mathf.sin(radians) * ballSpeed);
                }
            }

            ballVelocity.plus(gravity.getX() * deltaTime, gravity.getY() * deltaTime);
            ballPos.plus(ballVelocity.getX() * deltaTime, ballVelocity.getY() * deltaTime);
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
            GLES10.glTranslatef(cannonPos.getX(), cannonPos.getY(), 0);
            GLES10.glRotatef(cannonAngle, 0, 0, 1);
            GLES10.glColor4f(1, 1, 1, 1);
            cannonVertices.bind();
            cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
            cannonVertices.unbind();

            GLES10.glLoadIdentity();
            GLES10.glTranslatef(ballPos.getX(), ballPos.getY(), 0);
            GLES10.glColor4f(1, 0, 0, 1);
            ballVertices.bind();
            ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
            ballVertices.unbind();
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
