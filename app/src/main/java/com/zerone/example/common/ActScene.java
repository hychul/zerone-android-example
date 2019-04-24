package com.zerone.example.common;

import com.zerone.android.Graphics;
import com.zerone.android.ZeroneActivity;
import com.zerone.core.Scene;

public abstract class ActScene extends Scene {

    protected final ZeroneActivity zerone;
    protected final Graphics graphics;

    public ActScene(ZeroneActivity zerone) {
        this.zerone = zerone;
        this.graphics = zerone.getGLGraphics();
    }
}
