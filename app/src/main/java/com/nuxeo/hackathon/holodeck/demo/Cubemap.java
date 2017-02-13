package com.nuxeo.hackathon.holodeck.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import com.nuxeo.hackathon.holodeck.core.Model;

import java.io.IOException;
import java.io.InputStream;

public class Cubemap extends Model {

    private final BitmapFactory.Options options = new BitmapFactory.Options();
    private final Bitmap[] bitmaps = new Bitmap[12];
    private final Rect rect = new Rect();
    private final int[] textures = new int[2];

    public Cubemap() {
        cube(100);
        material = 2;
        options.inJustDecodeBounds = false;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }

    public Cubemap load(String filename) {

        // TODO: decode bitmap asynchronously
        try {
            // decode bitmap
            InputStream is = assets.open(filename);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, true);
            int w = decoder.getWidth() / 12;
            int h = decoder.getHeight();
            for (int i = 0; i < 12; i++) {
                rect.set(i * w, 0, i * w + w, h);
                bitmaps[i] = decoder.decodeRegion(rect, options);
            }
            is.close();
            decoder.recycle();
        } catch (IOException e) {
            Log.e("Cubemap", "Error decoding bitmap: " + filename + ": " + e.getMessage());
        }

        // create textures
        GLES20.glDeleteTextures(2, textures, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glGenTextures(2, textures, 0);
        for (int i = 0; i < 2; i++) {
            int b = (i == 0) ? 0 : 6;
            GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textures[i]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, bitmaps[b + 0], 0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, bitmaps[b + 1], 0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, bitmaps[b + 2], 0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, bitmaps[b + 3], 0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, bitmaps[b + 4], 0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, bitmaps[b + 5], 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);
        }

        for (Bitmap bitmap : bitmaps) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }

        return this;
    }

    public int texture(int index) {
        return textures[(index < 0 || index > textures.length - 1) ? 0 : index];
    }

    @Override
    protected void draw() {
        shader.map(this);
    }
}
