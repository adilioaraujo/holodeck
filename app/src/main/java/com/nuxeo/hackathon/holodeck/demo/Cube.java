package com.nuxeo.hackathon.holodeck.demo;

import com.nuxeo.hackathon.holodeck.core.Model;

public class Cube extends Model {

    public Cube() {
        texture = app.browser.texture();
        material = 0;
        cube(0.5f);
        setPosition(0f, 0.3f, 2f);
    }

    @Override
    protected void step() {
        addRotation(0.5f, 0.2f, 0.77f);
    }
}
