package com.willowtreeapps.hyperion.core.internal;

import android.app.Application;

import com.willowtreeapps.hyperion.core.PluginSource;
import com.willowtreeapps.hyperion.core.PluginViewFactory;
import com.willowtreeapps.hyperion.core.PublicControl;
import com.willowtreeapps.hyperion.plugin.v1.ApplicationExtension;
import com.willowtreeapps.hyperion.plugin.v1.ForegroundManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
abstract class AppModule {


    @Provides
    static PluginSource providePluginSource(CoreComponentContainer container) {
        return container.getPluginSource();
    }

    @AppScope
    @Binds
    abstract Application.ActivityLifecycleCallbacks bindFilter(HyperionIgnoreFilter filter);

    @AppScope
    @Binds
    abstract ApplicationExtension bindApplicationExtension(ApplicationExtensionImpl extension);

    @AppScope
    @Binds
    abstract PublicControl bindPublicControl(PublicControlImpl control);

    @AppScope
    @Binds
    abstract ForegroundManager bindForegroundManager(ForegroundManagerImpl foregroundManager);

    @AppScope
    @Binds
    @IntoSet
    abstract LifecycleDelegate bindServiceDelegate(HyperionServiceLifecycleDelegate delegate);

    @AppScope
    @Binds
    @IntoSet
    abstract LifecycleDelegate bindComponentDelegate(InstallationLifecycleDelegate delegate);

    @AppScope
    @Binds
    @IntoSet
    abstract LifecycleDelegate bindForegroundDelegate(ForegroundManagerLifecycleDelegate delegate);

    @AppScope
    @Binds
    abstract PluginViewFactory bindPluginViewFactory(HyperionPluginView.Factory factory);

}