package com.hychul.zerone.android.input;

public class TouchEvent {

    public static final int TOUCH_NONE = -1;
    public static final int TOUCH_DOWN = 0;
    public static final int TOUCH_UP = 1;
    public static final int TOUCH_DRAGGED = 2;

    public int pointerId;

    public int phase;

    public float x;
    public float y;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (phase == TOUCH_DOWN)
            builder.append("touch down, ");
        else if (phase == TOUCH_DRAGGED)
            builder.append("touch dragged, ");
        else if (phase == TOUCH_UP)
            builder.append("touch up, ");
        else
            builder.append("touch none, ");
        builder.append(pointerId);
        builder.append(",");
        builder.append(x);
        builder.append(",");
        builder.append(y);
        return builder.toString();
    }
}
