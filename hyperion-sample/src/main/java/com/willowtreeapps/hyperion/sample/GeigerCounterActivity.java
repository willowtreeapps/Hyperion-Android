package com.willowtreeapps.hyperion.sample;

public class GeigerCounterActivity extends PluginActivity {

    @Override
    protected int getPluginImage() {
        return R.drawable.geiger_counter;
    }

    @Override
    protected int getPluginTitle() {
        return R.string.geiger_counter;
    }

    @Override
    protected int getPluginDescription() {
        return R.string.geiger_counter_desc;
    }

}