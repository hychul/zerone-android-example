package com.hychul.zerone;

import com.hychul.zerone.core.Scene;

public interface Zerone {

    Input getInput();

    FileIO getFileIO();

    Audio getAudio();

    void setScene(Scene scene);

    Scene getCurrentScene();

    Scene getStartScene();
}