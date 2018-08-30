package com.zerone.component;

import com.zerone.core.Component;
import com.zerone.math.Quaternion;
import com.zerone.math.Vector3;

public final class Transform extends Component {

    public final Vector3 position;
    public final Vector3 scale;
    public final Quaternion rotation;

    public Transform() {
        this(0.0f, 0.0f, 1.0f, 1.0f);
    }

    public Transform(float x, float y, float scaleX, float scaleY) {
        this.position = new Vector3(x, y, 0);
        this.scale = new Vector3(scaleX, scaleY, 0);
        this.rotation = new Quaternion();
    }
}
