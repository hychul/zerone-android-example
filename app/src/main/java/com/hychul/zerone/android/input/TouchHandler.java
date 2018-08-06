package com.hychul.zerone.android.input;

import android.view.MotionEvent;
import android.view.View;

import com.hychul.zerone.data.Pool;
import com.hychul.zerone.data.Pool.PoolObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class TouchHandler implements View.OnTouchListener {

    private static final int MAX_TOUCHPOINTS = 10;

    boolean[] isTouched = new boolean[MAX_TOUCHPOINTS];
    float[] touchX = new float[MAX_TOUCHPOINTS];
    float[] touchY = new float[MAX_TOUCHPOINTS];
    int[] id = new int[MAX_TOUCHPOINTS];

    Pool<TouchEvent> touchEventPool;
    List<TouchEvent> touchEvents = new ArrayList<>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<>();

    public TouchHandler(View view) {
        view.setOnTouchListener(this);

        touchEventPool = new Pool<>(new PoolObjectFactory<TouchEvent>() {
            public TouchEvent create() {
                return new TouchEvent();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
            int pointerCount = event.getPointerCount();

            for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
                if (pointerCount <= i) {
                    isTouched[i] = false;
                    id[i] = -1;
                    continue;
                }

                int pointerId = event.getPointerId(i);

                if (event.getAction() != MotionEvent.ACTION_MOVE && i != pointerIndex) {
                    continue;
                }

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        onTouchEvent(event, i, TouchEvent.TOUCH_DOWN);
                        isTouched[i] = true;
                        id[i] = pointerId;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_CANCEL:
                        onTouchEvent(event, i, TouchEvent.TOUCH_UP);
                        isTouched[i] = false;
                        id[i] = -1;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        onTouchEvent(event, i, TouchEvent.TOUCH_DRAGGED);
                        isTouched[i] = true;
                        id[i] = pointerId;
                        break;
                }
            }
            return true;
        }
    }

    private void onTouchEvent(MotionEvent event, int pointerIndex, int type) {
        TouchEvent touchEvent = touchEventPool.get();
        touchEvent.pointerId = event.getPointerId(pointerIndex);
        touchEvent.type = type;
        touchEvent.x = touchX[pointerIndex] = event.getX(pointerIndex);
        touchEvent.y = touchY[pointerIndex] = event.getY(pointerIndex);

        touchEventsBuffer.add(touchEvent);
    }

    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return false;
            else
                return isTouched[index];
        }
    }

    public float getTouchX(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchX[index];
        }
    }

    public float getTouchY(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchY[index];
        }
    }

    private int getIndex(int pointerId) {
        for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
            if (id[i] == pointerId) {
                return i;
            }
        }
        return -1;
    }

    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++)
                touchEventPool.put(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }
}
