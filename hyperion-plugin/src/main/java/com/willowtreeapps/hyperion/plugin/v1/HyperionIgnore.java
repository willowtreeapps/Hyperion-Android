package com.willowtreeapps.hyperion.plugin.v1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate {@link android.app.Activity} subclasses with this to prevent Hyperion from embedding itself
 * in its layout.
 *
 * This can be useful for plugins that wish to start their own {@link android.app.Activity}
 * subclass as part of their functionality.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HyperionIgnore {
}