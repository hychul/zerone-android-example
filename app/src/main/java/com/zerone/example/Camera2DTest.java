package com.zerone.example;

import android.opengl.GLES10;

import com.zerone.example.common.ActScene;
import com.zerone.example.common.Cannon;
import com.zerone.example.common.GameObject;
import com.zerone.example.common.SpatialHashGrid;
import com.zerone.android.ZeroneActivity;
import com.zerone.android.graphics.Camera2D;
import com.zerone.android.graphics.Vertices;
import com.zerone.android.input.TouchEvent;
import com.zerone.core.Scene;
import com.zerone.core.Time;
import com.zerone.math.Mathf;
import com.zerone.physics.OverlapTester;
import com.zerone.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Camera2DTest extends ZeroneActivity {

    @Override
    public Scene getStartScene() {
        return new Camera2DScene(this);
    }

    class Camera2DScene extends ActScene {

        static final int NUM_TARGETS = 20;
        static final float WORLD_WIDTH = 9.6f;
        static final float WORLD_HEIGHT = 4.8f;

        Cannon cannon;
        GameObject ball;
        List<GameObject> targets;
        SpatialHashGrid grid;

        Vertices cannonVertices;
        Vertices ballVertices;
        Vertices targetVertices;

        Vector2 touchPos = new Vector2();
        Vector2 gravity = new Vector2(0, -10);

        Camera2D camera;

        public Camera2DScene(ZeroneActivity zerone) {
            super(zerone);
        }

        @Override
        public void onCreate() {
            cannon = new Cannon(0, 0, 1, 1);
            ball = new GameObject(0, 0, 0.2f, 0.2f);
            targets = new ArrayList<GameObject>(NUM_TARGETS);
            grid = new SpatialHashGrid(WORLD_WIDTH, WORLD_HEIGHT, 2.5f);
            for (int i = 0; i < NUM_TARGETS; i++) {
                GameObject target = new GameObject(Mathf.random() * WORLD_WIDTH,
                                                   Mathf.random() * WORLD_HEIGHT,
                                                   0.5f, 0.5f);
                grid.insertStaticObject(target);
                targets.add(target);
            }

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

            targetVertices = new Vertices(4, 6, false, false);
            targetVertices.setVertices(new float[]{-0.25f, -0.25f, 0.0f,
                                                   0.25f, -0.25f, 0.0f,
                                                   0.25f, 0.25f, 0.0f,
                                                   -0.25f, 0.25f, 0.0f}, 0, 8);
            targetVertices.setIndices(new short[]{0, 1, 2, 2, 3, 0}, 0, 6);

            camera = new Camera2D(graphics, WORLD_WIDTH, WORLD_HEIGHT);
        }

        public void update() {
            float deltaTime = Time.Companion.getDeltaTime();
            List<TouchEvent> touchEvents = zerone.getInput().getTouchEvents();
            zerone.getInput().getKeyEvents();

            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);

                camera.touchToWorld(touchPos.set(event.x, event.y));

                cannon.angle = touchPos.sub(cannon.position).angle();

                if (event.phase == TouchEvent.TOUCH_UP) {
                    float radians = Mathf.toRadians(cannon.angle);
                    float ballSpeed = touchPos.len() * 2;
                    ball.position.set(cannon.position);
                    ball.velocity.setX(Mathf.cos(radians) * ballSpeed);
                    ball.velocity.setY(Mathf.sin(radians) * ballSpeed);
                    ball.bounds.getLowerLeft().set(ball.position.getX() - 0.1f, ball.position.getY() - 0.1f);
                }
            }

            ball.velocity.plus(gravity.getX() * deltaTime, gravity.getY() * deltaTime);
            ball.position.plus(ball.velocity.getX() * deltaTime, ball.velocity.getY() * deltaTime);
            ball.bounds.getLowerLeft().plus(ball.velocity.getX() * deltaTime, ball.velocity.getY() * deltaTime);

            List<GameObject> colliders = grid.getPotentialColliders(ball);
            len = colliders.size();
            for (int i = 0; i < len; i++) {
                GameObject collider = colliders.get(i);
                if (OverlapTester.overlapRectangles(ball.bounds, collider.bounds)) {
                    grid.removeObject(collider);
                    targets.remove(collider);
                }
            }

            if (ball.position.getY() > 0) {
                camera.position.set(ball.position);
                camera.zoom = 1 + ball.position.getY() / WORLD_HEIGHT;
            } else {
                camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
                camera.zoom = 1;
            }
        }

        public void draw() {
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
            camera.setViewport();

            GLES10.glColor4f(0, 1, 0, 1);
            targetVertices.bind();
            int len = targets.size();
            for (int i = 0; i < len; i++) {
                GameObject target = targets.get(i);
                GLES10.glLoadIdentity();
                GLES10.glTranslatef(target.position.getX(), target.position.getY(), 0);
                targetVertices.draw(GL10.GL_TRIANGLES, 0, 6);
            }
            targetVertices.unbind();

            GLES10.glLoadIdentity();
            GLES10.glTranslatef(ball.position.getX(), ball.position.getY(), 0);
            GLES10.glColor4f(1, 0, 0, 1);
            ballVertices.bind();
            ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
            ballVertices.unbind();

            GLES10.glLoadIdentity();
            GLES10.glTranslatef(cannon.position.getX(), cannon.position.getY(), 0);
            GLES10.glRotatef(cannon.angle, 0, 0, 1);
            GLES10.glColor4f(1, 1, 1, 1);
            cannonVertices.bind();
            cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
            cannonVertices.unbind();
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
