package com.willowtreeapps.hyperion.core.internal;

import com.willowtreeapps.hyperion.core.ActivityResults;

import dagger.Module;
import dagger.Provides;

@Module
class ActivityResultModule {
    private final ActivityResultsFragment fragment;

    public ActivityResultModule(ActivityResultsFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    public ActivityResults providesActivityResults() {
        return fragment;
    }
}
