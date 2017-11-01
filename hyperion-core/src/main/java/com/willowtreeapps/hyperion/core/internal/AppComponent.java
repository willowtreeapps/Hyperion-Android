package com.willowtreeapps.hyperion.core.internal;

import android.app.Application;

import dagger.Component;

@AppScope
@Component(modules = {
        AppModule.class,
        LifecycleModule.class
})
public interface AppComponent {

    Application getApplication();
    Lifecycle getLifecycle();

    final class Holder {
        private static volatile AppComponent INSTANCE;

        static void init(Application application, Lifecycle lifecycle) {
            if (INSTANCE == null) {
                synchronized (Holder.class) {
                    if (INSTANCE == null) {
                        INSTANCE = DaggerAppComponent.builder()
                                .appModule(new AppModule(application))
                                .lifecycleModule(new LifecycleModule(lifecycle))
                                .build();
                    }
                }
            }
        }

        public static AppComponent getInstance() {
            return INSTANCE;
        }

        private Holder() {
            throw new AssertionError("No instances.");
        }
    }

}