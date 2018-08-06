package com.hychul.example;

import android.opengl.GLES10;

import com.hychul.example.common.ActScene;
import com.hychul.example.common.GameObject;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Camera2D;
import com.hychul.zerone.android.graphics.Sprite;
import com.hychul.zerone.android.graphics.SpriteAnimation;
import com.hychul.zerone.android.graphics.SpriteBatcher;
import com.hychul.zerone.android.graphics.Texture;
import com.hychul.zerone.core.Scene;
import com.hychul.zerone.core.Time;
import com.hychul.zerone.math.Mathf;

import javax.microedition.khronos.opengles.GL10;

public class AnimationTest extends ZeroneActivity {

    static final float WORLD_WIDTH = 480f;
    static final float WORLD_HEIGHT = 320f;

    @Override
    public Scene getStartScene() {
        return new AnimationScene(this);
    }

    static class Caveman extends GameObject {
        public float walkingTime = 0;

        public Caveman(float x, float y, float width, float height) {
            super(x, y, width, height);
            this.position.set(Mathf.random() * WORLD_WIDTH,
                              Mathf.random() * WORLD_HEIGHT);
            this.velocity.set(Math.random() > 0.5f ? -5f : 5f, 0);
            this.walkingTime = Mathf.random() * 10;
        }

        public void update(float deltaTime) {
            position.add(velocity.x * deltaTime, velocity.y * deltaTime);
            if (position.x < 0) position.x = WORLD_WIDTH;
            if (position.x > WORLD_WIDTH) position.x = 0;
            walkingTime += deltaTime;
        }
    }

    class AnimationScene extends ActScene {

        static final int NUM_CAVEMEN = 10;

        Caveman[] cavemen;
        SpriteBatcher batcher;
        Camera2D camera;
        Texture texture;
        SpriteAnimation walkAnim;

        public AnimationScene(ZeroneActivity zerone) {
            super(zerone);
        }

        @Override
        public void onCreate() {
            cavemen = new Caveman[NUM_CAVEMEN];
            for (int i = 0; i < NUM_CAVEMEN; i++) {
                cavemen[i] = new Caveman(Mathf.random(), Mathf.random(), 1, 1);
            }
            batcher = new SpriteBatcher(NUM_CAVEMEN);
            camera = new Camera2D(graphics, WORLD_WIDTH, WORLD_HEIGHT);
        }

        @Override
        public void onResume() {
            texture = new Texture(zerone.getFileIO(), "walkanim.png");
            walkAnim = new SpriteAnimation(0.2f,
                                           new Sprite(texture, 0, 0, 64, 64),
                                           new Sprite(texture, 64, 0, 64, 64),
                                           new Sprite(texture, 128, 0, 64, 64),
                                           new Sprite(texture, 192, 0, 64, 64));
        }

        @Override
        public void update() {
            float deltaTime = Time.getDeltaTime();
            int len = cavemen.length;
            for (int i = 0; i < len; i++) {
                cavemen[i].update(deltaTime);
            }
        }

        @Override
        public void draw() {
            GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
            camera.setViewport();

            GLES10.glEnable(GL10.GL_BLEND);
            GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            GLES10.glEnable(GL10.GL_TEXTURE_2D);

            batcher.beginBatch(texture);
            int len = cavemen.length;
            for (int i = 0; i < len; i++) {
                Caveman caveman = cavemen[i];
                Sprite keyFrame = walkAnim.getKeyFrame(caveman.walkingTime, true);
                batcher.drawSprite(keyFrame, caveman.velocity.x < 0 ? 64 : -64, 64, caveman.position.x, caveman.position.y, 0, 0);
            }
            batcher.endBatch();
        }

        @Override
        public void onPause() {
        }

        @Override
        public void onDestroy() {
        }
    }
}
