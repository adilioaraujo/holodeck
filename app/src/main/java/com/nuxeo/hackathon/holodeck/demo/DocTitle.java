package com.nuxeo.hackathon.holodeck.demo;

import com.nuxeo.hackathon.holodeck.core.Model;

public class DocTitle extends Model {

    public DocTitle() {
        texture = app.browser.texture();
        material = 1;
        sprite(3f, 0.15f, 0f, 0.1f, 1f, 0.15f, 1f, 1f, 1f, 0.5f);
        setPosition(0f, -0.03f, -4f);
    }
}
