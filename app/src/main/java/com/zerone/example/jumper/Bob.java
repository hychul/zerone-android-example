package com.zerone.example.jumper;

import com.zerone.example.common.GameObject;

public class Bob extends GameObject {
    public static final int BOB_STATE_JUMP = 0;
    public static final int BOB_STATE_FALL = 1;
    public static final int BOB_STATE_HIT = 2;
    public static final float BOB_JUMP_VELOCITY = 11;
    public static final float BOB_MOVE_VELOCITY = 20;
    public static final float BOB_WIDTH = 0.8f;
    public static final float BOB_HEIGHT = 0.8f;

    int state;
    float stateTime;

    public Bob(float x, float y) {
        super(x, y, BOB_WIDTH, BOB_HEIGHT);
        state = BOB_STATE_FALL;
        stateTime = 0;
    }

    public void update(float deltaTime) {
        velocity.plus(World.gravity.getX() * deltaTime, World.gravity.getY() * deltaTime);
        position.plus(velocity.getX() * deltaTime, velocity.getY() * deltaTime);
        bounds.getLowerLeft().set(position).sub(bounds.getWidth() / 2, bounds.getHeight() / 2);

        if (velocity.getY() > 0 && state != BOB_STATE_HIT) {
            if (state != BOB_STATE_JUMP) {
                state = BOB_STATE_JUMP;
                stateTime = 0;
            }
        }

        if (velocity.getY() < 0 && state != BOB_STATE_HIT) {
            if (state != BOB_STATE_FALL) {
                state = BOB_STATE_FALL;
                stateTime = 0;
            }
        }

        if (position.getX() < 0)
            position.setX(World.WORLD_WIDTH);
        if (position.getX() > World.WORLD_WIDTH)
            position.setX(0);

        stateTime += deltaTime;
    }

    public void hitSquirrel() {
        velocity.set(0, 0);
        state = BOB_STATE_HIT;
        stateTime = 0;
    }

    public void hitPlatform() {
        velocity.setY(BOB_JUMP_VELOCITY);
        state = BOB_STATE_JUMP;
        stateTime = 0;
    }

    public void hitSpring() {
        velocity.setY(BOB_JUMP_VELOCITY * 1.5f);
        state = BOB_STATE_JUMP;
        stateTime = 0;
    }
}
