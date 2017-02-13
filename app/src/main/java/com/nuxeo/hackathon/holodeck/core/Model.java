package com.nuxeo.hackathon.holodeck.core;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class Model extends Node {

    protected FloatBuffer p; // positions
    protected FloatBuffer c; // colors
    protected FloatBuffer n; // normals
    protected FloatBuffer t; // texture coords
    protected IntBuffer i;   // indices
    protected int len;
    protected int mode = GLES20.GL_TRIANGLE_FAN;
    protected int texture;
    protected int material;

    private final float[] matrix = new float[16];
    private final float[] position = new float[3];
    private final float[] rotation = new float[3];
    private boolean changed = true;

    public Model() {
        Matrix.setIdentityM(matrix, 0);
    }

    public void setPosition(float x, float y, float z) {
        position[0] = x;
        position[1] = y;
        position[2] = z;
        changed = true;
    }

    public void addPosition(float x, float y, float z) {
        position[0] += x;
        position[1] += y;
        position[2] += z;
        changed = true;
    }

    public void setRotation(float x, float y, float z) {
        rotation[0] = x;
        rotation[1] = y;
        rotation[2] = z;
        changed = true;
    }

    public void addRotation(float x, float y, float z) {
        rotation[0] += x;
        rotation[1] += y;
        rotation[2] += z;
        changed = true;
    }

    public float[] getPosition() {
        return position;
    }

    public float[] getRotation() {
        return rotation;
    }

    @Override
    protected void draw() {
        shader.model(this);
    }

    protected float[] getMatrix() {
        if (changed) {
            Matrix.setIdentityM(matrix, 0);
            Matrix.translateM(matrix, 0, position[0], position[1], position[2]);
            Matrix.rotateM(matrix, 0, rotation[0], 1f, 0f, 0f);
            Matrix.rotateM(matrix, 0, rotation[1], 0f, 1f, 0f);
            Matrix.rotateM(matrix, 0, rotation[2], 0f, 0f, 1f);
            changed = false;
        }
        return matrix;
    }

    protected void sprite(float w, float h) {
        sprite(w, h, 1f, 1f, 1f, 1f);
    }

    protected void sprite(float w, float h, float r, float g, float b, float a) {
        w *= 0.5f;
        h *= 0.5f;
        p = buffer(-w, +h, 0f, -w, -h, 0f, +w, -h, 0f, +w, +h, 0f);
        c = buffer(r, g, b, a, r, g, b, a, r, g, b, a, r, g, b, a);
        n = buffer(0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f);
        t = buffer(0f, 0f, 0f, 1f, 1f, 1f, 1f, 0f);
        i = buffer(0, 1, 2, 3);
        len = 4;
        mode = GLES20.GL_TRIANGLE_FAN;
    }

    protected void sprite(float w, float h, float tl, float tt, float tr, float tb, float r, float g, float b, float a) {
        w *= 0.5f;
        h *= 0.5f;
        p = buffer(-w, +h, 0f, -w, -h, 0f, +w, -h, 0f, +w, +h, 0f);
        c = buffer(r, g, b, a, r, g, b, a, r, g, b, a, r, g, b, a);
        n = buffer(0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f);
        t = buffer(
            tl, tt,
            tl, tb,
            tr, tb,
            tr, tt
        );
        i = buffer(0, 1, 2, 3);
        len = 4;
        mode = GLES20.GL_TRIANGLE_FAN;
    }

    protected void cube(float size) {

        size *= 0.5f;

        p = buffer(
            -size, +size, +size,  -size, -size, +size,  +size, +size, +size,  -size, -size, +size,  +size, -size, +size,  +size, +size, +size,
            +size, +size, +size,  +size, -size, +size,  +size, +size, -size,  +size, -size, +size,  +size, -size, -size,  +size, +size, -size,
            +size, +size, -size,  +size, -size, -size,  -size, +size, -size,  +size, -size, -size,  -size, -size, -size,  -size, +size, -size,
            -size, +size, -size,  -size, -size, -size,  -size, +size, +size,  -size, -size, -size,  -size, -size, +size,  -size, +size, +size,
            -size, +size, -size,  -size, +size, +size,  +size, +size, -size,  -size, +size, +size,  +size, +size, +size,  +size, +size, -size,
            +size, -size, -size,  +size, -size, +size,  -size, -size, -size,  +size, -size, +size,  -size, -size, +size,  -size, -size, -size
        );

        c = buffer(
            1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f,  1f, 1f, 1f, 1f
        );

        n = buffer(
            0f, 0f, 1f,  0f, 0f, 1f,  0f, 0f, 1f,  0f, 0f, 1f,  0f, 0f, 1f,  0f, 0f, 1f,
            1f, 0f, 0f,  1f, 0f, 0f,  1f, 0f, 0f,  1f, 0f, 0f,  1f, 0f, 0f,  1f, 0f, 0f,
            0f, 0f, -1f,  0f, 0f, -1f,  0f, 0f, -1f,  0f, 0f, -1f,  0f, 0f, -1f,  0f, 0f, -1f,
            -1f, 0f, 0f,  -1f, 0f, 0f,  -1f, 0f, 0f,  -1f, 0f, 0f,  -1f, 0f, 0f,  -1f, 0f, 0f,
            0f, 1f, 0f,  0f, 1f, 0f,  0f, 1f, 0f,  0f, 1f, 0f,  0f, 1f, 0f,  0f, 1f, 0f,
            0f, -1f, 0f,  0f, -1f, 0f,  0f, -1f, 0f,  0f, -1f, 0f,  0f, -1f, 0f,  0f, -1f, 0f
        );

        t = buffer(
            0f, 1f,  1f, 1f,  1f, 0f,  0f, 0f,
            0f, 1f,  1f, 1f,  1f, 0f,  0f, 0f,
            0f, 1f,  1f, 1f,  1f, 0f,  0f, 0f,
            0f, 1f,  1f, 1f,  1f, 0f,  0f, 0f,
            0f, 1f,  1f, 1f,  1f, 0f,  0f, 0f,
            0f, 1f,  1f, 1f,  1f, 0f,  0f, 0f
        );

        i = buffer(
            0, 1, 2,
            3, 4, 5,
            6, 7, 8,
            9, 10, 11,
            12, 13, 14,
            15, 16, 17,
            18, 19, 20,
            21, 22, 23,
            24, 25, 26,
            27, 28, 29,
            30, 31, 32,
            33, 34, 35
        );

        len = 36;
        mode = GLES20.GL_TRIANGLES;
    }

    static protected FloatBuffer buffer(float... values) {
        FloatBuffer buffer = ByteBuffer.allocateDirect(values.length * Float.SIZE / Byte.SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(values).position(0);
        return buffer;
    }

    static protected IntBuffer buffer(int... values) {
        IntBuffer buffer = ByteBuffer.allocateDirect(values.length * Integer.SIZE / Byte.SIZE)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(values).position(0);
        return buffer;
    }
}
