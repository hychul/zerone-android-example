package com.hychul.example;

import com.hychul.example.jumper.Assets;
import com.hychul.example.jumper.MainMenuScene;
import com.hychul.example.jumper.Settings;
import com.hychul.zerone.android.ZeroneActivity;
import com.zerone.core.Scene;

public class SuperJumper extends ZeroneActivity {
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