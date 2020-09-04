package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.willowtreeapps.hyperion.core.BuildConfig;
import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

public class HyperionPluginView extends FrameLayout {

    @Inject
    Set<PluginModule> modules;

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

        final Comparator<PluginModule> comparator = new AlphabeticalComparator(getContext());
        final Set<PluginModule> sortedModules = new TreeSet<>(comparator);
        sortedModules.addAll(modules);

        final Context inflaterContext = new PluginExtensionContextWrapper(getContext(), pluginExtension);
        final LayoutInflater inflater = LayoutInflater.from(inflaterContext);
        for (PluginModule module : sortedModules) {
            View view = module.createPluginView(inflater, pluginListContainer);
            pluginListContainer.addView(view);
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