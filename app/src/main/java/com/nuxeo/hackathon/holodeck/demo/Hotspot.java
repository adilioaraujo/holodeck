package com.nuxeo.hackathon.holodeck.demo;

import com.nuxeo.hackathon.holodeck.core.Model;

public class Hotspot extends Model {

    private float radius = 0.20f;
    private int steps = 80;
    private int step;

    public Hotspot() {
        texture = app.browser.texture();
        material = 1;
        sprite(1f, 0.2f, 0.25f, 0f, 0.75f, 0.1f, 1f, 1f, 1f, 0.5f);
        setPosition(0f, -0.25f, 2f);
        setRotation(0f, 180f, 0f);
    }

    @Override
    protected void draw() {
        super.draw();
        step = (shader.isLookingAt(this, radius)) ? step + 1 : 0;
        if (step == steps) {
            app.assistant.assist("holodeck", "Do you want to change holodeck?", 1800);
        }
    }
}
