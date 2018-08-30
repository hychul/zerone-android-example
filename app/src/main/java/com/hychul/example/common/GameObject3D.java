package com.hychul.example.common;

import com.zerone.math.Sphere;
import com.zerone.math.Vector3;

public class GameObject3D {

    public final Vector3 position;
    public final Sphere bounds;
    public final Vector3 velocity;
    public final Vector3 accel;

    public GameObject3D(float x, float y, float z, float radius) {
        this.position = new Vector3(x, y, z);
        this.bounds = new Sphere(x, y, z, radius);
        this.velocity = new Vector3();
        this.accel = new Vector3();
    }
}
