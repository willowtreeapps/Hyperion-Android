package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.willowtreeapps.hyperion.core.BuildConfig;
import com.willowtreeapps.hyperion.core.PluginViewFactory;
import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

public class HyperionPluginView extends FrameLayout {

    @Inject
    Set<PluginModule> modules;

    @Inject
    PluginFilter filter;

    public HyperionPluginView(@NonNull Context context) {
        this(context, null);
    }

    public HyperionPluginView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HyperionPluginView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CoreComponent component = ComponentProvider.get(context);
        component.inject(this);

        inflate(context, R.layout.hype_view_plugin, this);
        final TextView versionText = findViewById(R.id.version_text);
        versionText.setText(context.getString(R.string.hype_version_text, BuildConfig.VERSION_NAME));

        LinearLayout pluginListContainer = findViewById(R.id.plugin_list_container);
        PluginExtensionImpl pluginExtension = new PluginExtensionImpl(component);
        setFitsSystemWindows(true);
        setId(R.id.hyperion_plugins);
        ViewCompat.setOnApplyWindowInsetsListener(this, new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                setPadding(
                        insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        insets.getSystemWindowInsetBottom());
                return insets;
            }
        });

        Set<PluginModule> filteredModules = filter.filter(modules);

        final Comparator<PluginModule> comparator = new AlphabeticalComparator(getContext());
        final Set<PluginModule> sortedModules = new TreeSet<>(comparator);
        sortedModules.addAll(filteredModules);

        final Context inflaterContext = new PluginExtensionContextWrapper(getContext(), pluginExtension);
        final LayoutInflater inflater = LayoutInflater.from(inflaterContext);
        for (PluginModule module : sortedModules) {
            View view = module.createPluginView(inflater, pluginListContainer);
            pluginListContainer.addView(view);
        }
    }

    @AppScope
    static final class Factory implements PluginViewFactory {
        private CoreComponentContainer container;
        private static final String ACTIVITY_RESULT_TAG = "hyperion_activity_result";

        @Inject
        Factory (CoreComponentContainer container) {
            this.container = container;
        }

        private CoreComponent getCoreComponent(Activity activity, boolean standalone) {
            final ViewGroup windowContentView = activity.getWindow().findViewById(android.R.id.content);
            final HyperionMenuController controller = new HyperionMenuController(windowContentView);

            FragmentManagerCompat fragmentManager = FragmentManagerCompat.create(activity);

            ActivityResults activityResults = fragmentManager.findFragmentByTag(ACTIVITY_RESULT_TAG);
            if (activityResults == null) {
                activityResults = fragmentManager.isSupport() ? new ActivityResultsSupportFragment() : new ActivityResultsFragment();
                fragmentManager.beginTransaction()
                        .add(activityResults, ACTIVITY_RESULT_TAG)
                        .commit();
            }

            PluginFilter filter = new IdentityPluginFilter();
            if(standalone) {
                filter = new StandalonePluginFilter();
            }

            CoreComponent component = DaggerCoreComponent.builder()
                    .appComponent(AppComponent.Holder.getInstance(activity))
                    .activity(activity)
                    .menuController(controller)
                    .container(windowContentView)
                    .activityResults(activityResults)
                    .pluginFilter(filter)
                    .build();

            container.putComponent(activity, component);
            return component;
        }

        @Override
        public View create(Activity activity) {
            return createInternal(activity, false, true);
        }

        HyperionPluginView createInternal(Activity activity, boolean bindToMenuController, boolean standalone) {
            CoreComponent component = getCoreComponent(activity, standalone);
            HyperionPluginView pluginView = new HyperionPluginView(new ComponentContextThemeWrapper(activity, component));
            if(bindToMenuController) {
                component.getMenuController().setPluginView(pluginView);
            }
            return pluginView;
        }

        @Override
        public void destroy(Activity activity) {
            container.removeComponent(activity);
        }
    }

    private static final class AlphabeticalComparator implements Comparator<PluginModule> {

        private final Context context;

        private AlphabeticalComparator(Context context) {
            this.context = context;
        }

        @Override
        public int compare(PluginModule left, PluginModule right) {
            String leftName = getName(left);
            String rightName = getName(right);
            return leftName.compareTo(rightName);
        }

        private String getName(PluginModule pluginModule) {
            int resName = pluginModule.getName();
            if (resName == R.string.hype_module_name) return pluginModule.getClass().getSimpleName();
            else return context.getString(resName);
        }
    }
}