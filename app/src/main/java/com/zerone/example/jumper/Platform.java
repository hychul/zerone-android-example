package com.zerone.example.jumper;

import com.zerone.example.common.GameObject;

public class Platform extends GameObject {
    public static final float PLATFORM_WIDTH = 2;
    public static final float PLATFORM_HEIGHT = 0.5f;
    public static final int PLATFORM_TYPE_STATIC = 0;
    public static final int PLATFORM_TYPE_MOVING = 1;
    public static final int PLATFORM_STATE_NORMAL = 0;
    public static final int PLATFORM_STATE_PULVERIZING = 1;
    public static final float PLATFORM_PULVERIZE_TIME = 0.2f * 4;
    public static final float PLATFORM_VELOCITY = 2;

    int type;
    int state;
    float stateTime;

    public Platform(int type, float x, float y) {
        super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        this.type = type;
        this.state = PLATFORM_STATE_NORMAL;
        this.stateTime = 0;
        if (type == PLATFORM_TYPE_MOVING) {
            velocity.setX(PLATFORM_VELOCITY);
        }
    }

    public void update(float deltaTime) {
        if (type == PLATFORM_TYPE_MOVING) {
            position.plus(velocity.getX() * deltaTime, 0);
            bounds.getLowerLeft().set(position).sub(PLATFORM_WIDTH / 2, PLATFORM_HEIGHT / 2);

            if (position.getX() < PLATFORM_WIDTH / 2) {
                velocity.setX(-velocity.getX());
                position.setX(PLATFORM_WIDTH / 2);
            }
            if (position.getX() > World.WORLD_WIDTH - PLATFORM_WIDTH / 2) {
                velocity.setX(-velocity.getX());
                position.setX(World.WORLD_WIDTH - PLATFORM_WIDTH / 2);
            }
        }

        stateTime += deltaTime;
    }

    public void pulverize() {
        state = PLATFORM_STATE_PULVERIZING;
        stateTime = 0;
        velocity.setX(0);
    }
}
