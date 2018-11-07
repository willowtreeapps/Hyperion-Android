package com.willowtreeapps.hyperion.sample;

public class ChuckActivity extends PluginActivity {

    @Override
    protected int getPluginImage() {
        return R.drawable.hchuck_icon;
    }

    @Override
    protected int getPluginTitle() {
        return R.string.hchuck_plugin_name;
    }

    @Override
    protected int getPluginDescription() {
        return R.string.attributes_inspector_desc;
    }
}