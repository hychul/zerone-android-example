package com.zerone.example.invaders;

import com.zerone.example.common.GameObject3D;

public class Shot extends GameObject3D {
    static float SHOT_VELOCITY = 10f;
    static float SHOT_RADIUS = 0.1f;

    public Shot(float x, float y, float z, float velocityZ) {
        super(x, y, z, SHOT_RADIUS);
        velocity.setZ(velocityZ);
    }

    public void update(float deltaTime) {
        position.setZ(position.getZ() + velocity.getZ() * deltaTime);
        bounds.center.set(position);
    }
}
