package com.hychul.example;

import android.opengl.GLES10;

import com.hychul.zerone.Input.TouchEvent;
import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Camera2D;
import com.hychul.zerone.android.graphics.Sprite;
import com.hychul.zerone.android.graphics.SpriteBatcher;
import com.hychul.zerone.android.graphics.Texture;
import com.hychul.zerone.core.GameObject;
import com.hychul.zerone.core.Scene;
import com.hychul.zerone.math.Mathf;
import com.hychul.zerone.math.OverlapTester;
import com.hychul.zerone.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class SpriteBatcherTest extends ZeroneActivity {

    public Scene getStartScene() {
        return new SpriteBatcherScene(this);
    }

    class SpriteBatcherScene extends GLScene {
        final int NUM_TARGETS = 20;
        final float WORLD_WIDTH = 9.6f;
        final float WORLD_HEIGHT = 4.8f;
        Cannon cannon;
        GameObject ball;
        List<GameObject> targets;
        SpatialHashGrid grid;

        Sprite cannonRegion;
        Sprite ballRegion;
        Sprite bobRegion;
        SpriteBatcher batcher;

        Vector2 touchPos = new Vector2();
        Vector2 gravity = new Vector2(0, -10);

        Camera2D camera;

        Texture texture;

        public SpriteBatcherScene(Zerone zerone) {
            super(zerone);

            cannon = new Cannon(0, 0, 1, 0.5f);
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

            batcher = new SpriteBatcher(100);

            camera = new Camera2D(graphics, WORLD_WIDTH, WORLD_HEIGHT);
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

            if (ball.position.y > 0) {
                camera.position.set(ball.position);
                camera.zoom = 1 + ball.position.y / WORLD_HEIGHT;
            } else {
                camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
                camera.zoom = 1;
            }
        }

        @Override
        public void render() {
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
            camera.setViewport();

            GLES10.glEnable(GL10.GL_BLEND);
            GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            GLES10.glEnable(GL10.GL_TEXTURE_2D);

            batcher.beginBatch(texture);

            int len = targets.size();
            for (int i = 0; i < len; i++) {
                GameObject target = targets.get(i);
                batcher.drawSprite(bobRegion, 0.5f, 0.5f, target.position.x, target.position.y, 0, 0);
            }

            batcher.drawSprite(ballRegion, 0.2f, 0.2f, ball.position.x, ball.position.y, 0, 0);
            batcher.drawSprite(cannonRegion, 1, 0.5f, cannon.position.x, cannon.position.y, 0, cannon.angle);
            batcher.endBatch();

        }

        @Override
        public void onPause() {
        }

        @Override
        public void onResume() {
            texture = new Texture(zerone.getFileIO(), "atlas.png");
            cannonRegion = new Sprite(texture, 0, 0, 64, 32);
            ballRegion = new Sprite(texture, 0, 32, 16, 16);
            bobRegion = new Sprite(texture, 32, 32, 32, 32);
        }

        @Override
        public void onDestroy() {
        }
    }
}
