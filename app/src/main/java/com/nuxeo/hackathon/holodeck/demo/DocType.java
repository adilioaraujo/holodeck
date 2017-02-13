package com.nuxeo.hackathon.holodeck.demo;

import com.nuxeo.hackathon.holodeck.core.Model;

public class DocType extends Model {

    public DocType() {
        texture = app.browser.texture();
        material = 1;
        sprite(1f, 0.125f, 0.25f, 0.70f, 0.75f, 0.75f, 1f, 1f, 1f, 0.01f);
        setPosition(0f, 1.75f, -3.15f);
        setRotation(45f, 0f, 0f);
    }

}
