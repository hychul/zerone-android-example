package com.hychul.zerone.core;

public abstract class SimulationUnit {

    private Scene scene;

    // TODO: Scene management

    // Lifecycle
    public abstract void onCreate();

    public abstract void onStart();

    public void onResume() {
        scene.onResume();
    }

    public abstract void onSimulate();

    public abstract void onPause();

    public abstract void onStop();

    public abstract void onDestory();
}
