package com.hychul.example.common;

import com.hychul.zerone.android.Graphics;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.core.Scene;

public abstract class ActScene extends Scene {

    protected final ZeroneActivity zerone;
    protected final Graphics graphics;

    public ActScene(ZeroneActivity zerone) {
        this.zerone = zerone;
        this.graphics = zerone.getGLGraphics();
    }
}
