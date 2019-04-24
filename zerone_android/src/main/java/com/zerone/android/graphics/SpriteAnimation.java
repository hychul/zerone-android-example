package com.zerone.android.graphics;

public class SpriteAnimation {

    final Sprite[] keyFrames;
    final float frameDuration;

    public SpriteAnimation(float frameDuration, Sprite... keyFrames) {
        this.frameDuration = frameDuration;
        this.keyFrames = keyFrames;
    }

    public Sprite getKeyFrame(float stateTime, boolean isLoop) {
        int frameNumber = (int) (stateTime / frameDuration);

        if (isLoop) {
            frameNumber = frameNumber % keyFrames.length;
        } else {
            frameNumber = Math.min(keyFrames.length - 1, frameNumber);
        }

        return keyFrames[frameNumber];
    }
}
