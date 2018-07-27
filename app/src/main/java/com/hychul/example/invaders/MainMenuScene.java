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

public class MainMenuScene extends GLScene {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Vector2 touchPoint;
    Rectangle playBounds;
    Rectangle settingsBounds;

    public MainMenuScene(Zerone zerone) {
        super(zerone);

        guiCam = new Camera2D(graphics, 480, 320);
        batcher = new SpriteBatcher(10);
        touchPoint = new Vector2();
        playBounds = new Rectangle(240 - 112, 100, 224, 32);
        settingsBounds = new Rectangle(240 - 112, 100 - 32, 224, 32);
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
            if (OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                zerone.setScene(new GameScene(zerone));
            }
            if (OverlapTester.pointInRectangle(settingsBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                zerone.setScene(new SettingsScene(zerone));
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
