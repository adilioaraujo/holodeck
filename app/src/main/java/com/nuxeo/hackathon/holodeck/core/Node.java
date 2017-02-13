package com.nuxeo.hackathon.holodeck.core;

import android.content.res.AssetManager;

public abstract class Node {

    static protected final App app = App.instance();
    static protected final AssetManager assets = App.assets;
    static protected final Shader shader = App.shader;

    protected void step() {

    }

    protected void draw() {

    }

}
