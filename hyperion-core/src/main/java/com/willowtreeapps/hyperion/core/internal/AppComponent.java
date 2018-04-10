package com.willowtreeapps.hyperion.core.internal;

import android.app.Application;
import android.content.Context;

import com.willowtreeapps.hyperion.core.PublicControl;
import com.willowtreeapps.hyperion.plugin.v1.ApplicationExtension;

import dagger.BindsInstance;
import dagger.Component;

@AppScope
@Component(modules = AppModule.class)
public interface AppComponent {

    Application getApplication();
    ApplicationExtension getApplicationExtension();
    Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks();
    PublicControl getPublicControl();
    PluginRepository getPluginRepository();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder app(Application application);

        AppComponent build();
    }

    final class Holder {
        private static volatile AppComponent instance;

        public static AppComponent getInstance(Context context) {
            if (instance == null) {
                synchronized (Holder.class) {
                    if (instance == null) {
                        instance = DaggerAppComponent.builder()
                                .app((Application) context.getApplicationContext())
                                .build();
                    }
                }
            }
            return instance;
        }

        private Holder() {
            throw new AssertionError("No instances.");
        }
    }
}