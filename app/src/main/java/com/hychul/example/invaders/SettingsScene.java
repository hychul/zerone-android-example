package com.hychul.example.invaders;

import android.opengl.GLES10;

import com.hychul.zerone.Input.TouchEvent;
import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.android.graphics.Camera2D;
import com.hychul.zerone.android.graphics.SpriteBatcher;
import com.hychul.zerone.math.OverlapTester;
import com.hychul.zerone.math.Rectangle;
import com.hychul.zerone.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class SettingsScene extends GLScene {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Vector2 touchPoint;
    Rectangle touchBounds;
    Rectangle accelBounds;
    Rectangle soundBounds;
    Rectangle backBounds;

    public SettingsScene(Zerone zerone) {
        super(zerone);
        guiCam = new Camera2D(glGraphics, 480, 320);
        batcher = new SpriteBatcher(10);
        touchPoint = new Vector2();

        touchBounds = new Rectangle(120 - 32, 160 - 32, 64, 64);
        accelBounds = new Rectangle(240 - 32, 160 - 32, 64, 64);
        soundBounds = new Rectangle(360 - 32, 160 - 32, 64, 64);
        backBounds = new Rectangle(32, 32, 64, 64);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> events = zerone.getInput().getTouchEvents();
        int len = events.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = events.get(i);
            if (event.type != TouchEvent.TOUCH_UP)
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
                zerone.setScene(new MainMenuScene(zerone));
            }
        }
    }

    @Override
    public void render() {
        GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewport();

        GLES10.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(Assets.background);
        batcher.drawSprite(240, 160, 480, 320, Assets.backgroundRegion);
        batcher.endBatch();

        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);
        batcher.drawSprite(240, 280, 224, 32, Assets.settingsRegion);
        batcher.drawSprite(120, 160, 64, 64,
                Settings.touchEnabled ? Assets.touchEnabledRegion : Assets.touchRegion);
        batcher.drawSprite(240, 160, 64, 64,
                Settings.touchEnabled ? Assets.accelRegion
                        : Assets.accelEnabledRegion);
        batcher.drawSprite(360, 160, 64, 64,
                Settings.soundEnabled ? Assets.soundEnabledRegion : Assets.soundRegion);
        batcher.drawSprite(32, 32, 64, 64, Assets.leftRegion);
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
