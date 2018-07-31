package com.hychul.zerone.android;

import com.hychul.zerone.core.Scene;

public abstract class GLScene extends Scene {

    protected final ZeroneActivity zerone;
    protected final Graphics graphics;

    public GLScene(ZeroneActivity zerone) {
        super();
        this.zerone = zerone;
        graphics = zerone.getGLGraphics();
    }
}
