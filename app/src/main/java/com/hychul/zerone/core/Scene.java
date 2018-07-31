package com.hychul.zerone.core;

import com.hychul.zerone.Zerone;

public abstract class Scene {

    protected final Zerone zerone;

    private final World world;

    public Scene(Zerone zerone) {
        this.zerone = zerone;

        this.world = new World();
    }

    public void onCreate() {
        // TODO: Load resources. Make it abstract
    }

    public abstract void onResume();

    public void update() {
        world.update();
    }

    public void draw() {

    }

    public abstract void onPause();

    public abstract void onDestroy();
}
