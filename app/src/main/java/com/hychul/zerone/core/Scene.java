package com.hychul.zerone.core;

import com.hychul.zerone.Zerone;

public abstract class Scene {

    protected final Zerone zerone;

    public Scene(Zerone zerone) {
        this.zerone = zerone;
    }

    public abstract void update(float deltaTime);

    // TODO: Toss this to RenderSystem
    public abstract void render(float deltaTime);

    public abstract void onPause();

    public abstract void onResume();

    public abstract void onDestroy();
}
