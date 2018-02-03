package com.willowtreeapps.hyperion.core.internal;

import android.app.Application;

import com.willowtreeapps.hyperion.core.PublicControl;
import com.willowtreeapps.hyperion.plugin.v1.ApplicationExtension;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;

import java.util.ServiceLoader;

import dagger.Component;

@AppScope
@Component(modules = {
        AppModule.class,
        LifecycleModule.class
})
public interface AppComponent {

    Application getApplication();
    ApplicationExtension getApplicationExtension();
    Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks();
    PublicControl getPublicControl();

    final class Holder {
        private static volatile AppComponent INSTANCE;

        static void init(Application application) {
            if (INSTANCE == null) {
                synchronized (Holder.class) {
                    if (INSTANCE == null) {
                        INSTANCE = DaggerAppComponent.builder()
                                .appModule(new AppModule(application))
                                .build();
                        for (Plugin plugin : ServiceLoader.load(Plugin.class)) {
                            plugin.create(application, INSTANCE.getApplicationExtension());
                        }
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