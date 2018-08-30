package com.zerone.core;

public abstract class Scene {

    protected final World world = new World();

    public abstract void onCreate();

    public abstract void onResume();

    public void update() {
        world.update();
    }

    public void draw() {

    }

    public abstract void onPause();

    public abstract void onDestroy();
}
