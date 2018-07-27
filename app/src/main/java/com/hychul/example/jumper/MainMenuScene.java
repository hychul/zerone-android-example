package com.hychul.example.jumper;

import android.opengl.GLES10;

import com.hychul.zerone.Input;
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
    Rectangle soundBounds;
    Rectangle playBounds;
    Rectangle highscoresBounds;
    Rectangle helpBounds;
    Vector2 touchPoint;

    public MainMenuScene(Zerone zerone) {
        super(zerone);
        guiCam = new Camera2D(graphics, 320, 480);
        batcher = new SpriteBatcher(100);
        soundBounds = new Rectangle(0, 0, 64, 64);
        playBounds = new Rectangle(160 - 150, 200 + 18, 300, 36);
        highscoresBounds = new Rectangle(160 - 150, 200 - 18, 300, 36);
        helpBounds = new Rectangle(160 - 150, 200 - 18 - 36, 300, 36);
        touchPoint = new Vector2();
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = zerone.getInput().getTouchEvents();
        zerone.getInput().getKeyEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                touchPoint.set(event.x, event.y);
                guiCam.touchToWorld(touchPoint);

                if (OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    zerone.setScene(new GameScene(zerone));
                    return;
                }
                if (OverlapTester.pointInRectangle(highscoresBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    zerone.setScene(new HighscoreScene(zerone));
                    return;
                }
                if (OverlapTester.pointInRectangle(helpBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    zerone.setScene(new HelpScene(zerone));
                    return;
                }
                if (OverlapTester.pointInRectangle(soundBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if (Settings.soundEnabled)
                        Assets.music.play();
                    else
                        Assets.music.pause();
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

        batcher.drawSprite(Assets.logo, 274, 142, 160, 480 - 10 - 71, 0, 0);
        batcher.drawSprite(Assets.mainMenu, 300, 110, 160, 200, 0, 0);
        batcher.drawSprite(Settings.soundEnabled ? Assets.soundOn : Assets.soundOff, 64, 64, 32, 32, 0, 0);

        batcher.endBatch();

        GLES10.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void onPause() {
        Settings.save(zerone.getFileIO());
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onDestroy() {
    }
}
