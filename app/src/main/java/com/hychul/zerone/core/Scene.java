package com.hychul.zerone.core;

import com.hychul.zerone.Zerone;

public abstract class Scene {

    protected final Zerone zerone;

    private final World world;

    private Renderer renderer;

    public Scene(Zerone zerone) {
        this.zerone = zerone;

        this.world = new World();
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void onCreate() {
        // TODO: Load resources. Make it abstract
    }

    public abstract void onResume();

    public void update(float deltaTime) {
        world.update();
    }

    public void draw() {
        renderer.draw();
    }

    public abstract void onPause();

    public abstract void onDestroy();

    public interface Renderer {
        void draw();
    }
}
