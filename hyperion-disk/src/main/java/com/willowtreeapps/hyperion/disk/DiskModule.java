package com.willowtreeapps.hyperion.disk;

import dagger.Module;
import dagger.Provides;

@Module
class DiskModule {

    private final String path;

    DiskModule(String path) {
        this.path = path;
    }

    @Provides
    Files provideFiles() {
        return new Files(path);
    }

}