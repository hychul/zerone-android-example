package com.hychul.example;

import android.opengl.GLES10;

import com.hychul.example.common.ActScene;
import com.hychul.example.common.Cannon;
import com.hychul.example.common.GameObject;
import com.hychul.example.common.SpatialHashGrid;
import com.zerone.android.ZeroneActivity;
import com.zerone.android.graphics.Vertices;
import com.zerone.android.input.TouchEvent;
import com.zerone.core.Scene;
import com.zerone.core.Time;
import com.zerone.math.Mathf;
import com.zerone.math.OverlapTester;
import com.zerone.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class CollisionTest extends ZeroneActivity {

    @Override
    public Scene getStartScene() {
        return new CollisionScene(this);
    }

    class CollisionScene extends ActScene {

        final int NUM_TARGETS = 20;
        final float WORLD_WIDTH = 9.6f;
        final float WORLD_HEIGHT = 4.8f;

        Cannon cannon;
        GameObject ball;
        List<GameObject> targets;
        SpatialHashGrid grid;

        Vertices cannonVertices;
        Vertices ballVertices;
        Vertices targetVertices;

        Vector2 touchPos = new Vector2();
        Vector2 gravity = new Vector2(0, -10);

        public CollisionScene(ZeroneActivity zerone) {
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
        }

        public void update() {
            float deltaTime = Time.getDeltaTime();
            List<TouchEvent> touchEvents = zerone.getInput().getTouchEvents();
            zerone.getInput().getKeyEvents();

            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);

                touchPos.x = (event.x / (float) graphics.getWidth()) * WORLD_WIDTH;
                touchPos.y = (1 - event.y / (float) graphics.getHeight()) * WORLD_HEIGHT;

                cannon.angle = touchPos.sub(cannon.position).angle();

                if (event.phase == TouchEvent.TOUCH_UP) {
                    float radians = Mathf.toRadians(cannon.angle);
                    float ballSpeed = touchPos.len() * 2;
                    ball.position.set(cannon.position);
                    ball.velocity.x = Mathf.cos(radians) * ballSpeed;
                    ball.velocity.y = Mathf.sin(radians) * ballSpeed;
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
        }

        public void draw() {
            GLES10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
            GLES10.glMatrixMode(GL10.GL_PROJECTION);
            GLES10.glLoadIdentity();
            GLES10.glOrthof(0, WORLD_WIDTH, 0, WORLD_HEIGHT, 1, -1);
            GLES10.glMatrixMode(GL10.GL_MODELVIEW);

            GLES10.glColor4f(0, 1, 0, 1);
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
            GLES10.glColor4f(1, 0, 0, 1);
            ballVertices.bind();
            ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
            ballVertices.unbind();

            GLES10.glLoadIdentity();
            GLES10.glTranslatef(cannon.position.x, cannon.position.y, 0);
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
