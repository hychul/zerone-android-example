package com.hychul.zerone.android.graphics;

import com.zerone.math.Mathf;

import javax.microedition.khronos.opengles.GL10;

public class SpriteBatcher {

    final boolean hasColor;

    final float[] verticesBuffer;
    final Vertices vertices;

    int numSprites;
    int bufferIndex;

    public SpriteBatcher(int maxSprites) {
        this (maxSprites, false);
    }

    public SpriteBatcher(int maxSprites, boolean hasColor) {
        this.hasColor = hasColor;

        this.verticesBuffer = new float[maxSprites * (3 + (hasColor ? 4 : 0) + 2) * 4];
        this.vertices = new Vertices(maxSprites * 4, maxSprites * 6, hasColor, true);

        this.numSprites = 0;
        this.bufferIndex = 0;

        short[] indices = new short[maxSprites * 6];
        int len = indices.length;
        short j = 0;
        for (int i = 0; i < len; i += 6, j += 4) {
            indices[i + 0] = (short) (j + 0);
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 2);
            indices[i + 3] = (short) (j + 2);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] = (short) (j + 0);
        }
        vertices.setIndices(indices, 0, indices.length);
    }

    public void beginBatch(Texture texture) {
        texture.bind();

        numSprites = 0;
        bufferIndex = 0;
    }

    public void drawSprite(Sprite sprite, float width, float height, float x, float y, float z, float angle) {
        if (Mathf.abs(angle) < 0.001f)
            draw(sprite, width, height, x, y, z, 1, 1, 1, 1);
        else
            draw(sprite, width, height, x, y, z, angle, 1, 1, 1, 1);
    }

    public void drawSprite(Sprite sprite, float width, float height, float x, float y, float z, float angle, float r, float g, float b, float a) {
        if (Mathf.abs(angle) < 0.001f)
            draw(sprite, width, height, x, y, z, r, g, b, a);
        else
            draw(sprite, width, height, x, y, z, angle, r, g, b, a);
    }

    private void draw(Sprite sprite, float width, float height, float x, float y, float z, float r, float g, float b, float a) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;

        float x1 = x - halfWidth;
        float y1 = y - halfHeight;
        float x2 = x + halfWidth;
        float y2 = y + halfHeight;

        addVerticeBuffer(x1, y1, z, r, g, b, a, sprite.u1, sprite.v2);
        addVerticeBuffer(x2, y1, z, r, g, b, a, sprite.u2, sprite.v2);
        addVerticeBuffer(x2, y2, z, r, g, b, a, sprite.u2, sprite.v1);
        addVerticeBuffer(x1, y2, z, r, g, b, a, sprite.u1, sprite.v1);

        numSprites++;
    }

    private void draw(Sprite sprite, float width, float height, float x, float y, float z, float angle, float r, float g, float b, float a) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;

        float rad = Mathf.toRadians(angle);
        float cos = Mathf.cos(rad);
        float sin = Mathf.sin(rad);

        float x1 = x - halfWidth * cos - (-halfHeight) * sin;
        float y1 = y - halfWidth * sin + (-halfHeight) * cos;

        float x2 = x + halfWidth * cos - (-halfHeight) * sin;
        float y2 = y + halfWidth * sin + (-halfHeight) * cos;

        float x3 = x + halfWidth * cos - halfHeight * sin;
        float y3 = y + halfWidth * sin + halfHeight * cos;

        float x4 = x - halfWidth * cos - halfHeight * sin;
        float y4 = y - halfWidth * sin + halfHeight * cos;

        addVerticeBuffer(x1, y1, z, r, g, b, a, sprite.u1, sprite.v2);
        addVerticeBuffer(x2, y2, z, r, g, b, a, sprite.u2, sprite.v2);
        addVerticeBuffer(x3, y3, z, r, g, b, a, sprite.u2, sprite.v1);
        addVerticeBuffer(x4, y4, z, r, g, b, a, sprite.u1, sprite.v1);

        numSprites++;
    }

    private void addVerticeBuffer(float x, float y, float z, float r, float g, float b, float a, float u, float v) {
        verticesBuffer[bufferIndex++] = x;
        verticesBuffer[bufferIndex++] = y;
        verticesBuffer[bufferIndex++] = z;

        if (hasColor) {
            verticesBuffer[bufferIndex++] = r;
            verticesBuffer[bufferIndex++] = g;
            verticesBuffer[bufferIndex++] = b;
            verticesBuffer[bufferIndex++] = a;
        }

        verticesBuffer[bufferIndex++] = u;
        verticesBuffer[bufferIndex++] = v;
    }

    public void endBatch() {
        vertices.setVertices(verticesBuffer, 0, bufferIndex);
        vertices.bind();
        vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
        vertices.unbind();
    }
}
