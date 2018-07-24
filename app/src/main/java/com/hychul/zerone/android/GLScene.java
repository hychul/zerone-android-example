package com.hychul.zerone.android;

import com.hychul.zerone.Zerone;
import com.hychul.zerone.core.Scene;

public abstract class GLScene extends Scene {

    protected final ZeroneActivity zeroneActivity;
    protected final GLGraphics glGraphics;
    
    public GLScene(Zerone zerone) {
        super(zerone);
        zeroneActivity = (ZeroneActivity) zerone;
        glGraphics = ((ZeroneActivity) zerone).getGLGraphics();
    }
}
