package com.willowtreeapps.hyperion.core.internal;

import android.content.ServiceConnection;

import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;
import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;
import com.willowtreeapps.hyperion.plugin.v1.PluginExtension;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.Set;

import dagger.Binds;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

@Module
abstract class CoreModule {

    @Binds
    @ActivityScope
    abstract ServiceConnection bindServiceConnection(HyperionService.Connection connection);

    @Binds
    @ActivityScope
    abstract MeasurementHelper bindMeasurementHelper(MeasurementHelperImpl measurementHelper);

    @Binds
    @ActivityScope
    abstract AttributeTranslator bindAttributeTranslator(AttributeTranslatorImpl attributeTranslator);


}