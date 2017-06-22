package com.willowtreeapps.hyperion.disk;

import android.os.FileObserver;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class Files {

    private static final Handler main = new Handler(Looper.getMainLooper());
    private final String root;
    private String path;
    private List<File> files;
    private FileObserver observer;
    private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();

    Files(String root) {
        this.root = root;
        setPath(root);
        updateFiles();
    }

    boolean atRoot() {
        return root.equals(path);
    }

    void pop() {
        setPath(path.substring(0, path.lastIndexOf('/')));
    }

    void setPath(String path) {
        this.path = path;
        if (observer != null) {
            observer.stopWatching();
        }
        updateFiles();
        notifyFiles();
        observer = new FileObserver(path) {
            @Override
            public void onEvent(int event, String path) {
                switch (event & ALL_EVENTS) {
                    case MODIFY:
                    case CREATE:
                    case DELETE:
                        updateFiles();
                        notifyFiles();
                }
            }
        };
        toggleObserver();
    }

    @NonNull
    List<File> getFiles() {
        return files;
    }

    private void updateFiles() {
        File file = new File(path);
        files = Arrays.asList(file.listFiles());
    }

    private void notifyFiles() {
        main.post(new Runnable() {
            @Override
            public void run() {
                for (Listener listener : listeners) {
                    listener.onFilesChanged(files);
                }
            }
        });
    }

    void addListener(Listener listener) {
        listeners.add(listener);
        toggleObserver();
        listener.onFilesChanged(files);
    }

    void removeListener(Listener listener) {
        listeners.remove(listener);
        toggleObserver();
    }

    private void toggleObserver() {
        if (observer != null) {
            if (listeners.isEmpty()) {
                observer.stopWatching();
            } else {
                observer.startWatching();
            }
        }
    }

    interface Listener {
        void onFilesChanged(List<File> files);
    }

}