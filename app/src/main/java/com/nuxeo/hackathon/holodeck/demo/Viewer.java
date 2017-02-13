package com.nuxeo.hackathon.holodeck.demo;

import com.nuxeo.hackathon.holodeck.core.Model;

public class Viewer extends Model {

    public Viewer() {
        texture = app.browser.texture();
        material = 1;
        sprite(6f, 3.3f, 0f, 0.15f, 1f, 0.7f, 1f, 1f, 1f, 0.925f);
        setPosition(0f, 1.3f, -5.75f);
    }
}
