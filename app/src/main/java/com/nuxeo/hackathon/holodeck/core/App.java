package com.nuxeo.hackathon.holodeck.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import com.google.vr.sdk.base.*;

import javax.microedition.khronos.egl.EGLConfig;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.view.KeyEvent.*;

public class App {

    static private App instance;
    static private Scene scene;
    static AssetManager assets;
    static Shader shader;

    public Assistant assistant;
    public Vibrator vibrator;
    public Browser browser;
    public GvrView gvrview;

    private Class sceneClass;

    public static App instance() {
        return instance;
    }

    public App(GvrActivity activity, GvrView gvrView, Browser browser, Class mainSceneClass) {
        instance = this;
        this.sceneClass = mainSceneClass;

        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        assets = activity.getAssets();
        assistant = new Assistant(activity);

        gvrview = gvrView;
        gvrview.setKeepScreenOn(true);
        gvrview.setTransitionViewEnabled(false);
        gvrview.setDistortionCorrectionEnabled(true);
        gvrview.setNeckModelEnabled(true);
        gvrview.enableCardboardTriggerEmulation();
        if (gvrview.setAsyncReprojectionEnabled(true)) {
            AndroidCompat.setSustainedPerformanceMode(activity, true);
        }
        gvrview.setRenderer(renderer);
        activity.setGvrView(gvrview);

        this.browser = browser;
    }

    public void onResume() {
        gvrview.onResume();
    }

    public void onPause() {
        assistant.onPause();
        gvrview.onPause();
    }

    public void onDestroy() {
        assistant.onDestroy();
        gvrview.shutdown();
    }

    public void onEvent(final String id) {
        gvrview.queueEvent(new Runnable() {
            @Override
            public void run() {
                scene.event(id);
            }
        });
    }

    public boolean onKey(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {

            switch (event.getKeyCode()) {
                case KEYCODE_DPAD_CENTER:
                case KEYCODE_SPACE:
                case KEYCODE_ENTER:
                case KEYCODE_NUMPAD_ENTER:
                    browser.js("app.show();");
                    return true;

                case KEYCODE_MEDIA_FAST_FORWARD:
                case KEYCODE_SYSTEM_NAVIGATION_UP:
                case KEYCODE_DPAD_UP:
                case KEYCODE_CHANNEL_UP:
                case KEYCODE_PAGE_UP:
                    browser.js("app.show();");
                    return true;

                case KEYCODE_MEDIA_REWIND:
                case KEYCODE_SYSTEM_NAVIGATION_DOWN:
                case KEYCODE_DPAD_DOWN:
                case KEYCODE_CHANNEL_DOWN:
                case KEYCODE_PAGE_DOWN:
                    browser.js("app.back();");
                    return true;

                case KEYCODE_MEDIA_PREVIOUS:
                case KEYCODE_SYSTEM_NAVIGATION_LEFT:
                case KEYCODE_DPAD_LEFT:
                    browser.js("app.prev();");
                    return true;

                case KEYCODE_MEDIA_NEXT:
                case KEYCODE_SYSTEM_NAVIGATION_RIGHT:
                case KEYCODE_DPAD_RIGHT:
                    browser.js("app.next();");
                    return true;

                case KEYCODE_BACK:
                    browser.js("app.back();");
                    return true;

                case KEYCODE_VOLUME_UP:
                    gvrview.recenterHeadTracker();
                    break;
            }
        }

        return false;
    }

    private final GvrView.StereoRenderer renderer = new GvrView.StereoRenderer() {

        @Override
        public void onSurfaceCreated(EGLConfig config) {
            browser.init(512, 512);
            shader = new Shader(file("shader.vert"), file("shader.frag"));
            try {
                scene = (Scene) sceneClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                Log.e("App", "onSurfaceCreated: " + e.getMessage());
            }
            System.gc();
        }

        @Override
        public void onSurfaceChanged(int width, int height) {

        }

        @Override
        public void onNewFrame(HeadTransform headTransform) {
            browser.step();
            shader.step(headTransform);
            scene.step();
        }

        @Override
        public void onDrawEye(Eye eye) {
            shader.eye(eye);
            scene.draw();
        }

        @Override
        public void onFinishFrame(Viewport viewport) {

        }

        @Override
        public void onRendererShutdown(){

        }
    };

    private String file(String filename) {
        String output = "";
        try {
            InputStream inputStream = assets.open(filename);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int count;
            count = inputStream.read();
            while (count != -1) {
                byteArrayOutputStream.write(count);
                count = inputStream.read();
            }
            inputStream.close();
            output = byteArrayOutputStream.toString();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            Log.e("App", "Error reading file from assets: " + e.getMessage());
        }
        return output;
    }

}
