package com.hychul.zerone.core;

import com.hychul.zerone.Zerone;

public abstract class Scene {

    protected final Zerone zerone;

    protected final World world;

    public Scene(Zerone zerone) {
        this.zerone = zerone;

        this.world = new World();
    }

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
