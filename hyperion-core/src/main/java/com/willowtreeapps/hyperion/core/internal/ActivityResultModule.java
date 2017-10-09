package com.willowtreeapps.hyperion.core.internal;

import com.willowtreeapps.hyperion.core.ActivityResults;

import dagger.Module;
import dagger.Provides;

@Module
class ActivityResultModule {
    private final ActivityResults results;

    public ActivityResultModule(ActivityResults results) {
        this.results = results;
    }

    @Provides
    public ActivityResults providesActivityResults() {
        return results;
    }
}
