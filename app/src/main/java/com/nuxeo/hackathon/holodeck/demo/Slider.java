package com.nuxeo.hackathon.holodeck.demo;

import com.nuxeo.hackathon.holodeck.core.Model;

public class Slider extends Model {

    public Slider() {
        texture = app.browser.texture();
        material = 1;
        sprite(2.9f, 0.6f, 0.05f, 0.75f, 0.95f, 0.95f, 1f, 1f, 1f, 0.5f);
        setPosition(0f, -0.5f, -2.85f);
        setRotation(-50f, 0f, 0f);
    }
}
