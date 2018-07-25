package com.hychul.zerone.android.graphics;

import com.hychul.zerone.math.Mathf;

import javax.microedition.khronos.opengles.GL10;

public class SpriteBatcher {

    final float[] verticesBuffer;
    int bufferIndex;
    final Vertices vertices;
    int numSprites;

    public SpriteBatcher(int maxSprites) {
        this.verticesBuffer = new float[maxSprites * (3 + 2) * 4];
        this.vertices = new Vertices(maxSprites * 4, maxSprites * 6, false, true);
        this.bufferIndex = 0;
        this.numSprites = 0;

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
        // TODO: Change float comparison
        if (angle == 0)
            draw(sprite, width, height, x, y, z);
        else
            draw(sprite, width, height, x, y, z, angle);

    }

    private void draw(Sprite sprite, float width, float height, float x, float y, float z) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;

        float x1 = x - halfWidth;
        float y1 = y - halfHeight;
        float x2 = x + halfWidth;
        float y2 = y + halfHeight;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = sprite.u1;
        verticesBuffer[bufferIndex++] = sprite.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = sprite.u2;
        verticesBuffer[bufferIndex++] = sprite.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = sprite.u2;
        verticesBuffer[bufferIndex++] = sprite.v1;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = sprite.u1;
        verticesBuffer[bufferIndex++] = sprite.v1;

        numSprites++;
    }

    private void draw(Sprite sprite, float width, float height, float x, float y, float z, float angle) {
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

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = sprite.u1;
        verticesBuffer[bufferIndex++] = sprite.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = sprite.u2;
        verticesBuffer[bufferIndex++] = sprite.v2;

        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = sprite.u2;
        verticesBuffer[bufferIndex++] = sprite.v1;

        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = sprite.u1;
        verticesBuffer[bufferIndex++] = sprite.v1;

        numSprites++;
    }

    public void endBatch() {
        vertices.setVertices(verticesBuffer, 0, bufferIndex);
        vertices.bind();
        vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
        vertices.unbind();
    }
}
