package com.willowtreeapps.hyperion.disk;

import android.os.FileObserver;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class Files extends FileObserver {

    private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();
    private final File dir;
    private List<File> files;
    private boolean watching;

    Files(String path) {
        super(path);
        this.dir = new File(path);
        updateFiles();
    }

    @NonNull
    List<File> getFiles() {
        return files;
    }

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

    private void updateFiles() {
        files = Arrays.asList(dir.listFiles());
    }

    private void notifyFiles() {
        for (Listener listener : listeners) {
            listener.onFilesChanged(files);
        }
    }

    void addListener(Listener listener) {
        listeners.add(listener);
        listener.onFilesChanged(files);
        if (!watching && listeners.size() > 0) {
            watching = true;
            startWatching();
        }
    }

    void removeListener(Listener listener) {
        listeners.remove(listener);
        if (listeners.isEmpty()) {
            stopWatching();
        }
    }

    interface Listener {
        void onFilesChanged(List<File> files);
    }

}