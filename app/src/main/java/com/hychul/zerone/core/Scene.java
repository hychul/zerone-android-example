package com.hychul.zerone.core;

import com.hychul.zerone.Zerone;

public abstract class Scene {

    protected final Zerone zerone;

    private boolean isLoaded;

    public Scene(Zerone zerone) {
        this.zerone = zerone;
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

    public abstract void update(float deltaTime);

    // TODO: Toss this to RenderSystem
    public abstract void render();
}
