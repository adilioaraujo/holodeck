package com.nuxeo.hackathon.holodeck.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.webkit.*;

public class Browser extends WebView {

    private SurfaceTexture surfaceTexture;
    private Surface surface;
    private int texture;
    private String javascript;
    private boolean ready;

    public Browser(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        CookieManager.setAcceptFileSchemeCookies(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(this, true);

        WebSettings settings = getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);

        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setScrollbarFadingEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setBackgroundColor(0x00000000);

        addJavascriptInterface(new JavaScriptInterface(), "android");

        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());

        refresh();
    }

    public void refresh() {
        loadUrl("file:///android_asset/web/index.html");
    }

    public int texture() {
        return texture;
    }

    public void js(String code) {
        javascript = code;
        post(postJavascript);
    }

    public void init(int width, int height) {

        // clear surface
        if (surface != null){
            surface.release();
        }
        if (surfaceTexture != null){
            surfaceTexture.release();
        }
        surface = null;
        surfaceTexture = null;

        // create texture
        int[] handles = new int[1];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glDeleteTextures(1, handles, 0);
        GLES20.glGenTextures(1, handles, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, handles[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        texture = handles[0];

        // create surface
        if (texture > 0){
            surfaceTexture = new SurfaceTexture(texture);
            surfaceTexture.setDefaultBufferSize(width, height);
            surface = new Surface(surfaceTexture);
        }
    }

    protected void step() {
        if (surfaceTexture != null) {
            surfaceTexture.updateTexImage();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!ready ) {
            //super.draw(canvas);
            return;
        }
        // get surface canvas
        Canvas surfaceCanvas = null;
        if (surface != null) {
            try {
                surfaceCanvas = surface.lockCanvas(null);
            } catch (Exception e){
                Log.e("GLWebView", "getCanvas: " + e);
            }
        }
        // draw surface canvas
        if (surfaceCanvas != null) {
            // translate canvas based on WebView scroll
            float scale = surfaceCanvas.getWidth() / (float) canvas.getWidth();
            surfaceCanvas.scale(scale, scale);
            surfaceCanvas.translate(-getScrollX(), -getScrollY());
            super.draw(surfaceCanvas);
        }
        // release surface canvas
        if (surfaceCanvas != null) {
            surface.unlockCanvasAndPost(surfaceCanvas);
        }
        surfaceCanvas = null;
    }

    private final Runnable postJavascript = new Runnable() {
        @Override
        public void run() {
            loadUrl("javascript:" + javascript);
        }
    };

    public class JavaScriptInterface {

        @JavascriptInterface
        public void ready() {
            ready = true;
        }

        @JavascriptInterface
        public void speak(String message){
            App.instance().assistant.speak(message);
        }
    }
}