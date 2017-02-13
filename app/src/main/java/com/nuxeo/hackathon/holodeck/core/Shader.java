package com.nuxeo.hackathon.holodeck.core;

import android.opengl.Matrix;
import android.util.Log;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;
import com.nuxeo.hackathon.holodeck.demo.Cubemap;

import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
import static android.opengl.GLES20.*;

public class Shader {

    private float[] matrixV = new float[16];
    private float[] matrixP = new float[16];
    private final float[] matrixMV = new float[16];
    private final float[] matrixMVP = new float[16];
    private final float[] matrixHV = new float[16];

    private final float[] lightPositionInWorldSpace = new float[] { 0.0f, 2.0f, 0.0f, 1.0f };
    private final float[] lightPosition = new float[4];

    private final float[] tempPosition = new float[4];
    private final float[] POS_MATRIX_MULTIPLY_VEC = {0, 0, 0, 1.0f};
    private final float PROJECTION_NEAR = 0.1f;
    private final float PROJECTION_FAR = 100.0f;

    private final int program;
    private final int aPosition;
    private final int aNormal;
    private final int aColor;
    private final int aTexture;
    private final int uMV;
    private final int uMVP;
    private final int uLightPos;
    private final int uMaterial;
    private final int uTexture1;
    private final int uTexture2;

    private int eye;

    public Shader(String vertex, String fragment) {
        program = glCreateProgram();
        glAttachShader(program, createShader(GL_VERTEX_SHADER, vertex));
        glAttachShader(program, createShader(GL_FRAGMENT_SHADER, fragment));
        glLinkProgram(program);
        glUseProgram(program);

        aPosition = glGetAttribLocation(program, "aPosition");
        aNormal = glGetAttribLocation(program, "aNormal");
        aColor = glGetAttribLocation(program, "aColor");
        aTexture = glGetAttribLocation(program, "aTexture");

        uMV = glGetUniformLocation(program, "uMV");
        uMVP = glGetUniformLocation(program, "uMVP");
        uLightPos = glGetUniformLocation(program, "uLightPos");
        uMaterial = glGetUniformLocation(program, "uMaterial");
        uTexture1 = glGetUniformLocation(program, "uTexture1");
        uTexture2 = glGetUniformLocation(program, "uTexture2");
    }

    public void step(HeadTransform head) {
        head.getHeadView(matrixHV, 0);
    }

    public void eye(Eye eye) {
        glClearColor(0.1f, 0.1f, 0.1f, 0.5f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glUseProgram(program);

        this.eye = eye.getType();
        matrixV = eye.getEyeView();
        matrixP = eye.getPerspective(PROJECTION_NEAR, PROJECTION_FAR);
    }

    public void model(Model model) {
        Matrix.multiplyMM(matrixMV, 0, matrixV, 0, model.getMatrix(), 0);
        Matrix.multiplyMM(matrixMVP, 0, matrixP, 0, matrixMV, 0);
        glUniformMatrix4fv(uMV, 1, false, matrixMV, 0);
        glUniformMatrix4fv(uMVP, 1, false, matrixMVP, 0);

        Matrix.multiplyMV(lightPosition, 0, matrixV, 0, lightPositionInWorldSpace, 0);
        glUniform3fv(uLightPos, 1, lightPosition, 0);

        glUniform1i(uMaterial, model.material);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_EXTERNAL_OES, model.texture);
        glUniform1i(uTexture1, 1);

        glEnableVertexAttribArray(aPosition);
        glEnableVertexAttribArray(aNormal);
        glEnableVertexAttribArray(aColor);
        glEnableVertexAttribArray(aTexture);

        glVertexAttribPointer(aPosition, 3, GL_FLOAT, false, 0, model.p);
        glVertexAttribPointer(aNormal, 3, GL_FLOAT, false, 0, model.n);
        glVertexAttribPointer(aColor, 4, GL_FLOAT, false, 0, model.c);
        glVertexAttribPointer(aTexture, 2, GL_FLOAT, false, 0, model.t);

        glDrawElements(model.mode, model.len, GL_UNSIGNED_INT, model.i);

        glDisableVertexAttribArray(aPosition);
        glDisableVertexAttribArray(aNormal);
        glDisableVertexAttribArray(aColor);
        glDisableVertexAttribArray(aTexture);
    }

    public void map(Cubemap model) {
        Matrix.multiplyMM(matrixMV, 0, matrixV, 0, model.getMatrix(), 0);
        Matrix.multiplyMM(matrixMVP, 0, matrixP, 0, matrixMV, 0);
        glUniformMatrix4fv(uMVP, 1, false, matrixMVP, 0);

        glUniform1i(uMaterial, model.material);
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_CUBE_MAP, model.texture(eye - 1));
        glUniform1i(uTexture2, 2);

        glEnableVertexAttribArray(aPosition);
        glEnableVertexAttribArray(aNormal);
        glEnableVertexAttribArray(aColor);
        glEnableVertexAttribArray(aTexture);

        glVertexAttribPointer(aPosition, 3, GL_FLOAT, false, 0, model.p);
        glVertexAttribPointer(aNormal, 3, GL_FLOAT, false, 0, model.n);
        glVertexAttribPointer(aColor, 4, GL_FLOAT, false, 0, model.c);
        glVertexAttribPointer(aTexture, 2, GL_FLOAT, false, 0, model.t);

        glDrawElements(model.mode, model.len, GL_UNSIGNED_INT, model.i);

        glDisableVertexAttribArray(aPosition);
        glDisableVertexAttribArray(aNormal);
        glDisableVertexAttribArray(aColor);
        glDisableVertexAttribArray(aTexture);
    }

    public boolean isLookingAt(Model model, float radius) {
        Matrix.multiplyMM(matrixMV, 0, matrixHV, 0, model.getMatrix(), 0);
        Matrix.multiplyMV(tempPosition, 0, matrixMV, 0, POS_MATRIX_MULTIPLY_VEC, 0);
        float pitch = (float) Math.atan2(tempPosition[1], -tempPosition[2]);
        float yaw = (float) Math.atan2(tempPosition[0], -tempPosition[2]);
        return Math.abs(pitch) < radius && Math.abs(yaw) < radius;
    }

    private int createShader(int type, String code) {
        int shader = glCreateShader(type);
        glShaderSource(shader, code);
        glCompileShader(shader);

        // get shader compilation status
        final int[] compileStatus = new int[1];
        glGetShaderiv(shader, GL_COMPILE_STATUS, compileStatus, 0);

        // delete shader if compilation fails
        if (compileStatus[0] == 0) {
            glDeleteShader(shader);
            shader = 0;
            Log.e("Shader", "Error compiling shader: " + glGetShaderInfoLog(shader));
        }

        return shader;
    }

}
