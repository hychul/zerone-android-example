package com.hychul.example.jumper;

import android.opengl.GLES10;

import com.hychul.example.common.ActScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Camera2D;
import com.hychul.zerone.android.graphics.SpriteBatcher;
import com.hychul.zerone.android.input.TouchEvent;
import com.hychul.zerone.core.SceneManager;
import com.hychul.zerone.math.OverlapTester;
import com.hychul.zerone.math.Rectangle;
import com.hychul.zerone.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class HighscoreScene extends ActScene {

    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle backBounds;
    Vector2 touchPoint;
    String[] highScores;
    float xOffset = 0;

    public HighscoreScene(ZeroneActivity zerone) {
        super(zerone);
    }

    @Override
    public void onCreate() {
        guiCam = new Camera2D(graphics, 320, 480);
        backBounds = new Rectangle(0, 0, 64, 64);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(100);
        highScores = new String[5];
        for (int i = 0; i < 5; i++) {
            highScores[i] = (i + 1) + ". " + Settings.highscores[i];
            xOffset = Math.max(highScores[i].length() * Assets.font.glyphWidth, xOffset);
        }
        xOffset = 160 - xOffset / 2;
    }

    @Override
    public void update() {
        List<TouchEvent> touchEvents = zerone.getInput().getTouchEvents();
        zerone.getInput().getKeyEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);

            if (event.phase == TouchEvent.TOUCH_UP) {
                if (OverlapTester.pointInRectangle(backBounds, touchPoint)) {
                    SceneManager.loadScene(new MainMenuScene(zerone));
                    return;
                }
            }
        }
    }

    @Override
    public void draw() {
        GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewport();

        GLES10.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(Assets.background);
        batcher.drawSprite(Assets.backgroundRegion, 320, 480, 160, 240, 0, 0);
        batcher.endBatch();

        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);
        batcher.drawSprite(Assets.highScoresRegion, 300, 33, 160, 360, 0, 0);

        float y = 240;
        for (int i = 4; i >= 0; i--) {
            Assets.font.drawText(batcher, highScores[i], xOffset, y);
            y += Assets.font.glyphHeight;
        }

        batcher.drawSprite(Assets.arrow, 64, 64, 32, 32, 0, 0);
        batcher.endBatch();

        GLES10.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
    }
}
