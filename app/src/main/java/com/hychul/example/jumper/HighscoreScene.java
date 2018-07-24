package com.hychul.example.jumper;

import android.opengl.GLES10;

import com.hychul.zerone.Input;
import com.hychul.zerone.Zerone;
import com.hychul.zerone.android.GLScene;
import com.hychul.zerone.graphics.Camera2D;
import com.hychul.zerone.graphics.SpriteBatcher;
import com.hychul.zerone.math.OverlapTester;
import com.hychul.zerone.math.Rectangle;
import com.hychul.zerone.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class HighscoreScene extends GLScene {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle backBounds;
    Vector2 touchPoint;
    String[] highScores;  
    float xOffset = 0;

    public HighscoreScene(Zerone zerone) {
        super(zerone);
        
        guiCam = new Camera2D(glGraphics, 320, 480);
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
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = zerone.getInput().getTouchEvents();
        zerone.getInput().getKeyEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (OverlapTester.pointInRectangle(backBounds, touchPoint)) {
                    zerone.setScene(new MainMenuScene(zerone));
                    return;
                }
            }
        }
    }

    @Override
    public void render(float deltaTime) {
        GLES10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();
        
        GLES10.glEnable(GL10.GL_TEXTURE_2D);
        
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(160, 240, 320, 480, Assets.backgroundRegion);
        batcher.endBatch();
        
        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.items);
        batcher.drawSprite(160, 360, 300, 33, Assets.highScoresRegion);
        
        float y = 240;
        for (int i = 4; i >= 0; i--) {
            Assets.font.drawText(batcher, highScores[i], xOffset, y);
            y += Assets.font.glyphHeight;
        }
        
        batcher.drawSprite(32, 32, 64, 64, Assets.arrow);
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
