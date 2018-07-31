package com.hychul.example.jumper;

import android.opengl.GLES10;

import com.hychul.zerone.Input;
import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Camera2D;
import com.hychul.zerone.android.graphics.Sprite;
import com.hychul.zerone.android.graphics.SpriteBatcher;
import com.hychul.zerone.android.graphics.Texture;
import com.hychul.zerone.core.SceneManager;
import com.hychul.zerone.math.OverlapTester;
import com.hychul.zerone.math.Rectangle;
import com.hychul.zerone.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class HelpScene2 extends GLScene {

    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle nextBounds;
    Vector2 touchPoint;
    Texture helpImage;
    Sprite helpRegion;

    public HelpScene2(ZeroneActivity zerone) {
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
        helpImage = new Texture(zerone.getFileIO(), "jumper/help2.png");
        helpRegion = new Sprite(helpImage, 0, 0, 320, 480);
    }

    @Override
    public void onPause() {
        helpImage.dispose();
    }

    @Override
    public void update() {
        List<Input.TouchEvent> touchEvents = zerone.getInput().getTouchEvents();
        zerone.getInput().getKeyEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);

            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (OverlapTester.pointInRectangle(nextBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    SceneManager.loadScene(new HelpScene3(zerone));
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

