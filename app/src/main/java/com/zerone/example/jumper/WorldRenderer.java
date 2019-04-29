package com.zerone.example.jumper;

import android.opengl.GLES10;

import com.zerone.android.Graphics;
import com.zerone.android.graphics.Camera2D;
import com.zerone.android.graphics.Sprite;
import com.zerone.android.graphics.SpriteBatcher;

import javax.microedition.khronos.opengles.GL10;

public class WorldRenderer {

    static final float FRUSTUM_WIDTH = 10;
    static final float FRUSTUM_HEIGHT = 15;
    Graphics graphics;
    World world;
    Camera2D cam;
    SpriteBatcher batcher;

    public WorldRenderer(Graphics graphics, SpriteBatcher batcher, World world) {
        this.graphics = graphics;
        this.world = world;
        this.cam = new Camera2D(graphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;
    }

    public void render() {
        if (world.bob.position.getY() > cam.position.getY())
            cam.position.setY(world.bob.position.getY());
        cam.setViewport();
        renderBackground();
        renderObjects();
    }

    public void renderBackground() {
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(Assets.backgroundRegion, FRUSTUM_WIDTH, FRUSTUM_HEIGHT, cam.position.getX(), cam.position.getY(), 0, 0);
        batcher.endBatch();
    }

    public void renderObjects() {
        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);
        renderBob();
        renderPlatforms();
        renderItems();
        renderSquirrels();
        renderCastle();
        batcher.endBatch();
        GLES10.glDisable(GL10.GL_BLEND);
    }

    private void renderBob() {
        Sprite keyFrame;
        switch (world.bob.state) {
            case Bob.BOB_STATE_FALL:
                keyFrame = Assets.bobFall.getKeyFrame(world.bob.stateTime, true);
                break;
            case Bob.BOB_STATE_JUMP:
                keyFrame = Assets.bobJump.getKeyFrame(world.bob.stateTime, true);
                break;
            case Bob.BOB_STATE_HIT:
            default:
                keyFrame = Assets.bobHit;
        }

        float side = world.bob.velocity.getX() < 0 ? -1 : 1;
        batcher.drawSprite(keyFrame, side * 1, 1, world.bob.position.getX(), world.bob.position.getY(), 0, 0);
    }

    private void renderPlatforms() {
        int len = world.platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = world.platforms.get(i);
            Sprite keyFrame = Assets.platform;
            if (platform.state == Platform.PLATFORM_STATE_PULVERIZING) {
                keyFrame = Assets.brakingPlatform.getKeyFrame(platform.stateTime, false);
            }

            batcher.drawSprite(keyFrame, 2, 0.5f, platform.position.getX(), platform.position.getY(), 0, 0);
        }
    }

    private void renderItems() {
        int len = world.springs.size();
        for (int i = 0; i < len; i++) {
            Spring spring = world.springs.get(i);
            batcher.drawSprite(Assets.spring, 1, 1, spring.position.getX(), spring.position.getY(), 0, 0);
        }

        len = world.coins.size();
        for (int i = 0; i < len; i++) {
            Coin coin = world.coins.get(i);
            Sprite keyFrame = Assets.coinAnim.getKeyFrame(coin.stateTime, true);
            batcher.drawSprite(keyFrame, 1, 1, coin.position.getX(), coin.position.getY(), 0, 0);
        }
    }

    private void renderSquirrels() {
        int len = world.squirrels.size();
        for (int i = 0; i < len; i++) {
            Squirrel squirrel = world.squirrels.get(i);
            Sprite keyFrame = Assets.squirrelFly.getKeyFrame(squirrel.stateTime, true);
            float side = squirrel.velocity.getX() < 0 ? -1 : 1;
            batcher.drawSprite(keyFrame, side * 1, 1, squirrel.position.getX(), squirrel.position.getY(), 0, 0);
        }
    }

    private void renderCastle() {
        Castle castle = world.castle;
        batcher.drawSprite(Assets.castle, 2, 2, castle.position.getX(), castle.position.getY(), 0, 0);
    }
}
