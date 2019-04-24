package com.zerone.android.graphics;

public class Sprite {

    public final Texture texture;
    public final float u1, v1;
    public final float u2, v2;
    // TODO: Add anchor position

    public Sprite(Texture texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.u1 = x / texture.width;
        this.v1 = y / texture.height;
        this.u2 = this.u1 + width / texture.width;
        this.v2 = this.v1 + height / texture.height;
    }
}
