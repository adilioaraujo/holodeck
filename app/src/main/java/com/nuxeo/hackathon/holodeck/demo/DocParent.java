package com.nuxeo.hackathon.holodeck.demo;

import com.nuxeo.hackathon.holodeck.core.Model;

public class DocParent extends Model {

    public DocParent() {
        texture = app.browser.texture();
        material = 1;
        sprite(2.2f, 0.11f, 0f, 0.95f, 1f, 1f, 1f, 1f, 1f, 0.3f);
        setPosition(0f, -0.8f, -2.65f);
        setRotation(10f, 0f, 0f);
    }
}
