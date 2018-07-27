package com.hychul.zerone.core;

import com.hychul.zerone.Zerone;

public abstract class Scene {

    protected final Zerone zerone;

    private final World world;

    private Renderer renderer;

    private boolean isLoaded;

    public Scene(Zerone zerone) {
        this.zerone = zerone;

        this.world = new World();
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public final void load() {
        onLoad();
        isLoaded = true;
    }

    public void onLoad() {
        // TODO: Load resources. Make it abstract
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onDestroy();

    public void update(float deltaTime) {
        world.update();
    }

    public void draw() {
        renderer.draw();
    }

    public interface Renderer {
        void draw();
    }
}
