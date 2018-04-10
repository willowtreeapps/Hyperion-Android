package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.willowtreeapps.hyperion.core.BuildConfig;
import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.plugin.v1.HyperionMenu;
import com.willowtreeapps.hyperion.plugin.v1.MenuState;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

public class HyperionPluginView extends FrameLayout {

    private final LinearLayout pluginListContainer;
    private final PluginExtensionImpl pluginExtension;

    @Inject
    PluginLoader pluginLoader;

    private Set<PluginModule> modules;
    private MenuState menuState = MenuState.CLOSE;

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

        pluginListContainer = findViewById(R.id.plugin_list_container);
        pluginExtension = new PluginExtensionImpl(component);
        setFitsSystemWindows(true);
        setId(R.id.hyperion_plugins);
        ViewCompat.setImportantForAccessibility(
                this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
        ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
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
    }

    void setMenuState(MenuState menuState) {
        this.menuState = menuState;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // block touches while the menu is closed.
        return menuState == MenuState.CLOSE || super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        pluginExtension.setHyperionMenu((HyperionMenu) getParent());
        pluginLoader.load(new Callback<Plugins>() {
            @Override
            public void call(Try<Plugins> result) {
                if (ViewCompat.isAttachedToWindow(HyperionPluginView.this)) {
                    populatePluginList(result);
                }
            }
        });
    }

    private void populatePluginList(Try<Plugins> result) {
        try {
            final Plugins plugins = result.get();
            final Comparator<PluginModule> comparator = new AlphabeticalComparator(getContext());
            final Set<PluginModule> sortedModules = new TreeSet<>(comparator);
            sortedModules.addAll(plugins.createModules());
            this.modules = sortedModules;
        } catch (Throwable t) {
            // TODO
            t.printStackTrace();
            return;
        }

        final Context context = new PluginExtensionContextWrapper(
                getContext(), pluginExtension);
        final LayoutInflater inflater = LayoutInflater.from(context);
        for (PluginModule module : modules) {
            module.create(pluginExtension, context);
            View view = module.createPluginView(inflater, pluginListContainer);
            pluginListContainer.addView(view);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (modules != null) {
            for (PluginModule module : modules) {
                module.destroy();
            }
        }
        pluginExtension.setHyperionMenu(null);
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