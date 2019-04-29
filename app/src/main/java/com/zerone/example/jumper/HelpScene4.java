package com.zerone.example.jumper;

import android.opengl.GLES10;

import com.zerone.example.common.ActScene;
import com.zerone.android.ZeroneActivity;
import com.zerone.android.graphics.Camera2D;
import com.zerone.android.graphics.Sprite;
import com.zerone.android.graphics.SpriteBatcher;
import com.zerone.android.graphics.Texture;
import com.zerone.android.input.TouchEvent;
import com.zerone.core.SceneManager;
import com.zerone.math.OverlapTester;
import com.zerone.math.Rectangle;
import com.zerone.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class HelpScene4 extends ActScene {

    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle nextBounds;
    Vector2 touchPoint;
    Texture helpImage;
    Sprite helpRegion;

    public HelpScene4(ZeroneActivity zerone) {
        super(zerone);
    }

    @Override
    public void onCreate() {
        guiCam = new Camera2D(graphics, 320, 480);
        nextBounds = new Rectangle(320 - 64, 0, 64, 64);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(1);
    }

    @Override
    public void onResume() {
        helpImage = new Texture(zerone.getFileIO(), "jumper/help4.png");
        helpRegion = new Sprite(helpImage, 0, 0, 320, 480);
    }

    @Override
    public void onPause() {
        helpImage.dispose();
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
                if (OverlapTester.pointInRectangle(nextBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    SceneManager.INSTANCE.loadScene(new HelpScene5(zerone));
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

        batcher.beginBatch(helpImage);
        batcher.drawSprite(helpRegion, 320, 480, 160, 240, 0, 0);
        batcher.endBatch();

        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);
        batcher.drawSprite(Assets.arrow, -64, 64, 320 - 32, 32, 0, 0);
        batcher.endBatch();

        GLES10.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void onDestroy() {
    }
}
