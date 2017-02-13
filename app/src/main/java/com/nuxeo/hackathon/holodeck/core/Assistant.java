package com.nuxeo.hackathon.holodeck.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.List;
import java.util.Locale;

public class Assistant extends UtteranceProgressListener implements RecognitionListener, TextToSpeech.OnInitListener {

    private TextToSpeech speech;
    private SpeechRecognizer recognizer;
    private Intent intent;
    private Handler handler;

    public Assistant(Context context) {
        handler = new Handler();
        speech = new TextToSpeech(context, this);
        speech.setOnUtteranceProgressListener(this);

        recognizer = SpeechRecognizer.createSpeechRecognizer(context);
        recognizer.setRecognitionListener(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    }

    public void speak(String message) {
        if (!speech.isSpeaking()) {
            speech.speak(message, TextToSpeech.QUEUE_FLUSH, null, "message");
        }
    }

    public void assist(String id, String message, long delay) {
        speech.setSpeechRate(1f);
        speech.speak(message, TextToSpeech.QUEUE_FLUSH, null, id);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recognizer.startListening(intent);
            }
        }, delay);
    }

    public void onPause() {
        if (speech != null) {
            speech.stop();
        }
        if (recognizer != null) {
            recognizer.stopListening();
        }
    }

    public void onDestroy() {
        if (recognizer != null) {
            recognizer.destroy();
            recognizer = null;
        }
        if (speech != null) {
            speech.shutdown();
        }
    }

    /**
     * TextToSpeech
     */

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            speech.setLanguage(Locale.US);
            speech.setPitch(0.8f);
            speech.setSpeechRate(1.1f);
            speech.speak("Welcome!!", TextToSpeech.QUEUE_FLUSH, null, "welcome");
        }
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {

    }

    @Override
    public void onError(String utteranceId) {

    }

    /**
     * SpeechRecognizer
     */

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        List<String> list = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (!list.isEmpty()) {
            String message = list.get(0).trim().toLowerCase();
            if (message.contains("yes")) {
                App.instance().onEvent("holodeck");
            }
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

}
