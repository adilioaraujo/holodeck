package com.nuxeo.hackathon.holodeck;

import android.Manifest;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.nuxeo.hackathon.holodeck.core.App;
import com.nuxeo.hackathon.holodeck.core.Browser;
import com.nuxeo.hackathon.holodeck.demo.MainScene;

public class MainActivity extends GvrActivity {

    private App app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions(new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE }, 0);

        setContentView(R.layout.main);

        final GvrView gvrview = (GvrView) findViewById(R.id.gvrview);
        final Browser browser = (Browser) findViewById(R.id.browser);

        app = new App(this, gvrview, browser, MainScene.class);

        TextView start = (TextView) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browser.refresh();
                view.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        app.onResume();
    }

    @Override
    public void onPause() {
        app.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        app.onDestroy();
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid()); // lame hack ;)
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return app.onKey(event);
    }

}
