package com.lailem.app.listener;

public interface OnAudioPlayListener {
    void onAudioPlayEnd(String tag, String path);

    void onAudioException();
}
