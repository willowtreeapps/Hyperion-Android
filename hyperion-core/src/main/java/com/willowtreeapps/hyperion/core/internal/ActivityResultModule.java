package com.willowtreeapps.hyperion.core.internal;

import com.willowtreeapps.hyperion.core.ActivityResults;

import dagger.Module;
import dagger.Provides;

@Module
class ActivityResultModule {
    private final ActivityResults results;

    ActivityResultModule(ActivityResults results) {
        this.results = results;
    }

    @Provides
    ActivityResults providesActivityResults() {
        return results;
    }
}
