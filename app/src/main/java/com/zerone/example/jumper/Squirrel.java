package com.zerone.example.jumper;

import com.zerone.example.common.GameObject;

public class Squirrel extends GameObject {
    public static final float SQUIRREL_WIDTH = 1;
    public static final float SQUIRREL_HEIGHT = 0.6f;
    public static final float SQUIRREL_VELOCITY = 3f;

    float stateTime = 0;

    public Squirrel(float x, float y) {
        super(x, y, SQUIRREL_WIDTH, SQUIRREL_HEIGHT);
        velocity.set(SQUIRREL_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.plus(velocity.getX() * deltaTime, velocity.getY() * deltaTime);
        bounds.getLowerLeft().set(position).sub(SQUIRREL_WIDTH / 2, SQUIRREL_HEIGHT / 2);

        if (position.getX() < SQUIRREL_WIDTH / 2) {
            position.setX(SQUIRREL_WIDTH / 2);
            velocity.setX(SQUIRREL_VELOCITY);
        }
        if (position.getX() > World.WORLD_WIDTH - SQUIRREL_WIDTH / 2) {
            position.setX(World.WORLD_WIDTH - SQUIRREL_WIDTH / 2);
            velocity.setX(-SQUIRREL_VELOCITY);
        }
        stateTime += deltaTime;
    }
}
