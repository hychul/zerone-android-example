package com.hychul.example;

import android.opengl.GLES10;

import com.hychul.zerone.graphics.Vertices3;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class HierarchicalObject {
    public float x, y, z;
    public float scale = 1;
    public float rotationY, rotationParent;
    public boolean hasParent;
    public final List<HierarchicalObject> children = new ArrayList<HierarchicalObject>();
    public final Vertices3 mesh;

    public HierarchicalObject(Vertices3 mesh, boolean hasParent) {
        this.mesh = mesh;
        this.hasParent = hasParent;
    }

    public void update(float deltaTime) {
        rotationY += 45 * deltaTime;
        rotationParent += 20 * deltaTime;
        int len = children.size();
        for (int i = 0; i < len; i++) {
            children.get(i).update(deltaTime);
        }
    }

    public void render() {
        GLES10.glPushMatrix();
        if (hasParent)
            GLES10.glRotatef(rotationParent, 0, 1, 0);
        GLES10.glTranslatef(x, y, z);
        GLES10.glPushMatrix();
        GLES10.glRotatef(rotationY, 0, 1, 0);
        GLES10.glScalef(scale, scale, scale);
        mesh.draw(GL10.GL_TRIANGLES, 0, 36);
        GLES10.glPopMatrix();

        int len = children.size();
        for (int i = 0; i < len; i++) {
            children.get(i).render();
        }
        GLES10.glPopMatrix();
    }
}
