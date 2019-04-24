package com.zerone.example;

import com.zerone.example.invaders.Assets;
import com.zerone.example.invaders.MainMenuScene;
import com.zerone.example.invaders.Settings;
import com.zerone.android.ZeroneActivity;
import com.zerone.core.Scene;

public class Invaders extends ZeroneActivity {
    boolean firstTimeCreate = true;

    public Scene getStartScene() {
        return new MainMenuScene(this);
    }

    @Override
    public void onInitialized() {
        if (firstTimeCreate) {
            Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreate = false;
        } else {
            Assets.reload();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Settings.soundEnabled)
            Assets.music.pause();
    }
}
