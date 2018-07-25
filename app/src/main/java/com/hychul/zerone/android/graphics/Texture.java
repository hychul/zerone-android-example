package com.hychul.zerone.android.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.opengl.GLES10;
import android.opengl.GLUtils;

import com.hychul.zerone.FileIO;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

public class Texture {

    FileIO fileIO;
    String filename;
    int textureId;
    int minFilter;
    int magFilter;
    public int width;
    public int height;
    boolean mipmapped;

    public Texture(FileIO fileIO, String filename) {
        this(fileIO, filename, false);
    }

    public Texture(FileIO fileIO, String filename, boolean mipmapped) {
        this.fileIO = fileIO;
        this.filename = filename;
        this.textureId = -1;
        this.mipmapped = mipmapped;
        load();
    }

    private void load() {
        textureId = newTextureId();

        InputStream in = null;
        try {
            in = fileIO.readAsset(filename);

            Bitmap bitmap = BitmapFactory.decodeStream(in);
            width = bitmap.getWidth();
            height = bitmap.getHeight();

            GLES10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

            if (mipmapped) {
                createMipmaps(bitmap);
            } else {
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
                setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
            }

            GLES10.glBindTexture(GL10.GL_TEXTURE_2D, 0);
            bitmap.recycle();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load texture '" + filename + "'", e);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {

                }
        }
    }

    private int newTextureId() {
        int[] textureIds = new int[1];
        GLES10.glGenTextures(1, textureIds, 0);
        return textureIds[0];
    }

    private void createMipmaps(Bitmap bitmap) {
        setFilters(GL10.GL_LINEAR_MIPMAP_NEAREST, GL10.GL_LINEAR);

        int level = 0;
        int newWidth = width;
        int newHeight = height;
        while (true) {
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
            newWidth = newWidth / 2;
            newHeight = newHeight / 2;
            if (newWidth <= 0)
                break;
            Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight,
                                                   bitmap.getConfig());
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(bitmap,
                              new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                              new Rect(0, 0, newWidth, newHeight), null);
            bitmap.recycle();
            bitmap = newBitmap;
            level++;
        }
    }

    public void reload() {
        load();
        bind();
        setFilters(minFilter, magFilter);
        GLES10.glBindTexture(GL10.GL_TEXTURE_2D, 0);
    }

    public void setFilters(int minFilter, int magFilter) {
        this.minFilter = minFilter;
        this.magFilter = magFilter;
        GLES10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
        GLES10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
    }

    public void bind() {
        GLES10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }

    public void dispose() {
        GLES10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        int[] textureIds = {textureId};
        GLES10.glDeleteTextures(1, textureIds, 0);
        textureId = -1;
    }
}
