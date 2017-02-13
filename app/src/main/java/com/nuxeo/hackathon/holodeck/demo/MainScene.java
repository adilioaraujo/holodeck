package com.nuxeo.hackathon.holodeck.demo;

import com.nuxeo.hackathon.holodeck.core.Scene;

public class MainScene extends Scene {

    private final Cubemap cubemap = new Cubemap();
    private final Cube cube = new Cube();
    private final Viewer viewer = new Viewer();
    private final Slider slider = new Slider();
    private final DocType type = new DocType();
    private final DocTitle title = new DocTitle();
    private final DocParent parent = new DocParent();
    private final Hotspot hotspot = new Hotspot();

    private final String[] holodecks = {"cubemap1.png", "cubemap2.png"};
    private int holodeck = 0;

    public MainScene() {
        cubemap.load(holodecks[holodeck]);
        add(cubemap, cube, viewer, slider, parent, title, type, hotspot);
    }

    @Override
    protected void event(String id) {
        if ("holodeck".equals(id)) {
            app.vibrator.vibrate(25);
            app.assistant.speak("Please wait. Loading holodeck...");
            holodeck = (holodeck == 0) ? 1 : 0;
            cubemap.load(holodecks[holodeck]);
            app.vibrator.vibrate(15);
            app.assistant.speak("Holodeck loaded");
        }
    }
}
