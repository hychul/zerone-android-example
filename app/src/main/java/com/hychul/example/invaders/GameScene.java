package com.hychul.example.invaders;

import android.opengl.GLES10;

import com.hychul.example.invaders.World.WorldListener;
import com.hychul.zerone.Input.TouchEvent;
import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Camera2D;
import com.hychul.zerone.android.graphics.FPSCounter;
import com.hychul.zerone.android.graphics.SpriteBatcher;
import com.hychul.zerone.core.SceneManager;
import com.hychul.zerone.core.Time;
import com.hychul.zerone.math.OverlapTester;
import com.hychul.zerone.math.Rectangle;
import com.hychul.zerone.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class GameScene extends GLScene {

    static final int GAME_RUNNING = 0;
    static final int GAME_PAUSED = 1;
    static final int GAME_OVER = 2;

    int state;
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;
    World world;
    WorldListener worldListener;
    WorldRenderer renderer;
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle quitBounds;
    Rectangle leftBounds;
    Rectangle rightBounds;
    Rectangle shotBounds;
    int lastScore;
    int lastLives;
    int lastWaves;
    String scoreString;
    FPSCounter fpsCounter;

    public GameScene(ZeroneActivity zerone) {
        super(zerone);
    }

    @Override
    public void onCreate() {
        state = GAME_RUNNING;
        guiCam = new Camera2D(graphics, 480, 320);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(100);
        world = new World();
        worldListener = new WorldListener() {
            public void shot() {
                Assets.playSound(Assets.shotSound);
            }

            public void explosion() {
                Assets.playSound(Assets.explosionSound);
            }
        };
        world.setWorldListener(worldListener);
        renderer = new WorldRenderer(graphics);
        pauseBounds = new Rectangle(480 - 64, 320 - 64, 64, 64);
        resumeBounds = new Rectangle(240 - 80, 160, 160, 32);
        quitBounds = new Rectangle(240 - 80, 160 - 32, 160, 32);
        shotBounds = new Rectangle(480 - 64, 0, 64, 64);
        leftBounds = new Rectangle(0, 0, 64, 64);
        rightBounds = new Rectangle(64, 0, 64, 64);
        lastScore = 0;
        lastLives = world.ship.lives;
        lastWaves = world.waves;
        scoreString = "lives:" + lastLives + " waves:" + lastWaves + " score:"
                      + lastScore;
        fpsCounter = new FPSCounter();
    }

    @Override
    public void update() {
        float deltaTime = Time.getDeltaTime();
        switch (state) {
            case GAME_PAUSED:
                updatePaused();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
            case GAME_OVER:
                updateGameOver();
                break;
        }
    }

    private void updatePaused() {
        List<TouchEvent> events = zerone.getInput().getTouchEvents();
        int len = events.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = events.get(i);
            if (event.type != TouchEvent.TOUCH_UP)
                continue;

            guiCam.touchToWorld(touchPoint.set(event.x, event.y));
            if (OverlapTester.pointInRectangle(resumeBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_RUNNING;
            }

            if (OverlapTester.pointInRectangle(quitBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                SceneManager.loadScene(new MainMenuScene(zerone));
            }
        }
    }

    private void updateRunning(float deltaTime) {
        List<TouchEvent> events = zerone.getInput().getTouchEvents();
        int len = events.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = events.get(i);
            if (event.type != TouchEvent.TOUCH_DOWN)
                continue;

            guiCam.touchToWorld(touchPoint.set(event.x, event.y));

            if (OverlapTester.pointInRectangle(pauseBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_PAUSED;
            }
            if (OverlapTester.pointInRectangle(shotBounds, touchPoint)) {
                world.shoot();
            }
        }

        world.update(deltaTime, calculateInputAcceleration());
        if (world.ship.lives != lastLives || world.score != lastScore
            || world.waves != lastWaves) {
            lastLives = world.ship.lives;
            lastScore = world.score;
            lastWaves = world.waves;
            scoreString = "lives:" + lastLives + " waves:" + lastWaves
                          + " score:" + lastScore;
        }
        if (world.isGameOver()) {
            state = GAME_OVER;
        }
    }

    private float calculateInputAcceleration() {
        float accelX = 0;
        if (Settings.touchEnabled) {
            for (int i = 0; i < 2; i++) {
                if (zerone.getInput().isTouchDown(i)) {
                    guiCam.touchToWorld(touchPoint.set(zerone.getInput()
                                                               .getTouchX(i), zerone.getInput().getTouchY(i)));
                    if (OverlapTester.pointInRectangle(leftBounds, touchPoint)) {
                        accelX = -Ship.SHIP_VELOCITY / 5;
                    }
                    if (OverlapTester.pointInRectangle(rightBounds, touchPoint)) {
                        accelX = Ship.SHIP_VELOCITY / 5;
                    }
                }
            }
        } else {
            accelX = zerone.getInput().getAccelY();
        }
        return accelX;
    }

    private void updateGameOver() {
        List<TouchEvent> events = zerone.getInput().getTouchEvents();
        int len = events.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = events.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                Assets.playSound(Assets.clickSound);
                SceneManager.loadScene(new MainMenuScene(zerone));
            }
        }
    }

    @Override
    public void draw() {
        GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        guiCam.setViewport();

        GLES10.glEnable(GL10.GL_TEXTURE_2D);
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(Assets.backgroundRegion, 480, 320, 240, 160, 0, 0);
        batcher.endBatch();
        GLES10.glDisable(GL10.GL_TEXTURE_2D);

        renderer.render(world);

        switch (state) {
            case GAME_RUNNING:
                renderRunning();
                break;
            case GAME_PAUSED:
                renderPaused();
                break;
            case GAME_OVER:
                renderGameOver();
        }

        fpsCounter.logFrame();
    }

    private void renderRunning() {
        guiCam.setViewport();
        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        GLES10.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(Assets.items);
        batcher.drawSprite(Assets.pauseButtonRegion, 64, 64, 480 - 32, 320 - 32, 0, 0);
        Assets.font.drawText(batcher, scoreString, 10, 320 - 20);
        if (Settings.touchEnabled) {
            batcher.drawSprite(Assets.leftRegion, 64, 64, 32, 32, 0, 0);
            batcher.drawSprite(Assets.rightRegion, 64, 64, 96, 32, 0, 0);
        }
        batcher.drawSprite(Assets.fireRegion, 64, 64, 480 - 40, 32, 0, 0);
        batcher.endBatch();

        GLES10.glDisable(GL10.GL_TEXTURE_2D);
        GLES10.glDisable(GL10.GL_BLEND);
    }

    private void renderPaused() {
        guiCam.setViewport();
        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        GLES10.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(Assets.items);
        Assets.font.drawText(batcher, scoreString, 10, 320 - 20);
        batcher.drawSprite(Assets.pauseRegion, 160, 64, 240, 160, 0, 0);
        batcher.endBatch();

        GLES10.glDisable(GL10.GL_TEXTURE_2D);
        GLES10.glDisable(GL10.GL_BLEND);
    }

    private void renderGameOver() {
        guiCam.setViewport();
        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        GLES10.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(Assets.items);
        batcher.drawSprite(Assets.gameOverRegion, 128, 64, 240, 160, 0, 0);
        Assets.font.drawText(batcher, scoreString, 10, 320 - 20);
        batcher.endBatch();

        GLES10.glDisable(GL10.GL_TEXTURE_2D);
        GLES10.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void onPause() {
        state = GAME_PAUSED;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
