package com.hychul.example.invaders;

import android.opengl.GLES10;

import com.hychul.example.common.ActScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Camera2D;
import com.hychul.zerone.android.graphics.SpriteBatcher;
import com.hychul.zerone.android.input.TouchEvent;
import com.zerone.core.SceneManager;
import com.zerone.math.OverlapTester;
import com.zerone.math.Rectangle;
import com.zerone.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class SettingsScene extends ActScene {

    Camera2D guiCam;
    SpriteBatcher batcher;
    Vector2 touchPoint;
    Rectangle touchBounds;
    Rectangle accelBounds;
    Rectangle soundBounds;
    Rectangle backBounds;

    public SettingsScene(ZeroneActivity zerone) {
        super(zerone);
    }

    @Override
    public void onCreate() {
        guiCam = new Camera2D(graphics, 480, 320);
        batcher = new SpriteBatcher(10);
        touchPoint = new Vector2();

        touchBounds = new Rectangle(120 - 32, 160 - 32, 64, 64);
        accelBounds = new Rectangle(240 - 32, 160 - 32, 64, 64);
        soundBounds = new Rectangle(360 - 32, 160 - 32, 64, 64);
        backBounds = new Rectangle(32, 32, 64, 64);
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
            if (OverlapTester.pointInRectangle(touchBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                Settings.touchEnabled = true;
                Settings.save(zerone.getFileIO());
            }
            if (OverlapTester.pointInRectangle(accelBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                Settings.touchEnabled = false;
                Settings.save(zerone.getFileIO());
            }
            if (OverlapTester.pointInRectangle(soundBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                Settings.soundEnabled = !Settings.soundEnabled;
                if (Settings.soundEnabled) {
                    Assets.music.play();
                } else {
                    Assets.music.pause();
                }
                Settings.save(zerone.getFileIO());
            }
            if (OverlapTester.pointInRectangle(backBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                SceneManager.loadScene(new MainMenuScene(zerone));
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
        batcher.drawSprite(Assets.settingsRegion, 224, 32, 240, 280, 0, 0);
        batcher.drawSprite(Settings.touchEnabled ? Assets.touchEnabledRegion : Assets.touchRegion,
                           64, 64, 120, 160, 0, 0);
        batcher.drawSprite(Settings.touchEnabled ? Assets.accelRegion : Assets.accelEnabledRegion,
                           64, 64, 240, 160, 0, 0);
        batcher.drawSprite(Settings.soundEnabled ? Assets.soundEnabledRegion : Assets.soundRegion,
                           64, 64, 360, 160, 0, 0);
        batcher.drawSprite(Assets.leftRegion, 64, 64, 32, 32, 0, 0);
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
