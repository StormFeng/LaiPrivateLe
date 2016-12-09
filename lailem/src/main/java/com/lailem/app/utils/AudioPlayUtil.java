package com.lailem.app.utils;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.lailem.app.adapter.PagerTabAdapter;
import com.lailem.app.listener.OnAudioPlayListener;

import java.io.IOException;
import java.util.HashMap;

public class AudioPlayUtil implements OnCompletionListener {

    private static AudioPlayUtil instance = null;
    Integer sessionId = 0;
    MediaPlayer mediaPlayer = null;
    HashMap<Integer, Model> map;

    public AudioPlayUtil() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        map = new HashMap<Integer, Model>();
    }

    public static AudioPlayUtil getInstance() {

        if (instance == null) {
            instance = new AudioPlayUtil();
        }
        return instance;
    }

    /**
     * @param tag                 可传空
     * @param path
     * @param onAudioPlayListener 可传空
     */
    public void start(String path, String tag, OnAudioPlayListener onAudioPlayListener) {
        try {
            Model oldModel = map.get(mediaPlayer.getAudioSessionId());
            if (oldModel != null) {
                if (!oldModel.tag.equals(tag)) {
                    oldModel.onAudioPlayListener.onAudioPlayEnd(oldModel.tag, oldModel.path);
                }
            }

            mediaPlayer.reset();
            mediaPlayer.setAudioSessionId(sessionId);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            if (onAudioPlayListener != null) {
                Model model = new Model();
                model.path = path;
                model.tag = tag;
                model.onAudioPlayListener = onAudioPlayListener;
                map.put(sessionId, model);
            }
            sessionId++;
        } catch (IOException e) {
            e.printStackTrace();
            if(onAudioPlayListener!=null){
                onAudioPlayListener.onAudioException();
            }
        }
    }


    public void start(String path, String tag, Object object, OnAudioPlayListener onAudioPlayListener) {
        try {
            Model oldModel = map.get(mediaPlayer.getAudioSessionId());
            if (oldModel != null) {
                if (!oldModel.tag.equals(tag)) {
                    oldModel.onAudioPlayListener.onAudioPlayEnd(oldModel.tag, oldModel.path);
                }
            }

            mediaPlayer.reset();
            mediaPlayer.setAudioSessionId(sessionId);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            if (onAudioPlayListener != null) {
                Model model = new Model();
                model.path = path;
                model.tag = tag;
                model.onAudioPlayListener = onAudioPlayListener;
                model.object = object;
                map.put(sessionId, model);
            }
            sessionId++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioSessionId(sessionId);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            sessionId++;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.reset();

    }

    public void seekTo(int msec) {
        mediaPlayer.seekTo(msec);
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public String getPlayingTag() {
        Model model = getPlayingModel();
        if (model == null) {
            return "";
        } else {
            return model.tag;
        }
    }

    public Object getPlayingObject() {
        Model model = getPlayingModel();
        if (model == null) {
            return null;
        } else {
            return model.object;
        }
    }

    public Object getPlayingPath() {
        Model model = getPlayingModel();
        if (model == null) {
            return "";
        } else {
            return model.path;
        }
    }

    public OnAudioPlayListener getPlayingAudioPlayListener() {
        Model model = getPlayingModel();
        if (model == null) {
            return null;
        } else {
            return model.onAudioPlayListener;
        }
    }

    public Model getPlayingModel() {
        if (mediaPlayer != null && isPlaying()) {
            return map.get(mediaPlayer.getAudioSessionId());
        }
        return null;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Model model = map.get(mediaPlayer.getAudioSessionId());
        if (model != null) {
            model.onAudioPlayListener.onAudioPlayEnd(model.tag, model.path);
        }
    }

    class Model {
        public String tag;
        public OnAudioPlayListener onAudioPlayListener;
        public String path;
        public Object object;
    }

}
