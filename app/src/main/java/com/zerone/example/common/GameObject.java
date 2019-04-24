package com.zerone.example.common;

import com.zerone.math.Rectangle;
import com.zerone.math.Vector2;

public class GameObject {

    public final Vector2 position;
    public final Rectangle bounds;
    public final Vector2 velocity;
    public final Vector2 accel;

    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle(x - width / 2, y - height / 2, width, height);
        this.velocity = new Vector2();
        this.accel = new Vector2();
    }
}
