package com.hychul.zerone;

import com.hychul.zerone.core.Scene;

public interface Zerone {

    // TODO: Static classes
    Input getInput();

    FileIO getFileIO();

    Audio getAudio();

    // TODO: SceneManager
    Scene getStartScene();
}