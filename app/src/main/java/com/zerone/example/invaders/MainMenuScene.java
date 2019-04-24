package com.zerone.example.invaders;

import android.opengl.GLES10;

import com.zerone.example.common.ActScene;
import com.zerone.android.ZeroneActivity;
import com.zerone.android.graphics.Camera2D;
import com.zerone.android.graphics.SpriteBatcher;
import com.zerone.android.input.TouchEvent;
import com.zerone.core.SceneManager;
import com.zerone.math.OverlapTester;
import com.zerone.math.Rectangle;
import com.zerone.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class MainMenuScene extends ActScene {

    Camera2D guiCam;
    SpriteBatcher batcher;
    Vector2 touchPoint;
    Rectangle playBounds;
    Rectangle settingsBounds;

    public MainMenuScene(ZeroneActivity zerone) {
        super(zerone);
    }

    @Override
    public void onCreate() {
        guiCam = new Camera2D(graphics, 480, 320);
        batcher = new SpriteBatcher(10);
        touchPoint = new Vector2();
        playBounds = new Rectangle(240 - 112, 100, 224, 32);
        settingsBounds = new Rectangle(240 - 112, 100 - 32, 224, 32);
    }

    @Override
    public void update() {
        List<TouchEvent> events = zerone.getInput().getTouchEvents();
        int len = events.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = events.get(i);
            if (event.phase != TouchEvent.TOUCH_UP)
                continue;

            guiCam.touchToWorld(touchPoint.set(event.x, event.y));
            if (OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                SceneManager.loadScene(new GameScene(zerone));
            }
            if (OverlapTester.pointInRectangle(settingsBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                SceneManager.loadScene(new SettingsScene(zerone));
            }
        }
    }

    @Override
    public void draw() {
        GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewport();

        GLES10.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(Assets.background);
        batcher.drawSprite(Assets.backgroundRegion, 480, 320, 240, 160, 0, 0);
        batcher.endBatch();

        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);
        batcher.drawSprite(Assets.logoRegion, 384, 128, 240, 240, 0, 0);
        batcher.drawSprite(Assets.menuRegion, 224, 64, 240, 100, 0, 0);
        batcher.endBatch();

        GLES10.glDisable(GL10.GL_BLEND);
        GLES10.glDisable(GL10.GL_TEXTURE_2D);
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
