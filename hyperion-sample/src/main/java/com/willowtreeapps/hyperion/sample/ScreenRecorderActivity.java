package com.willowtreeapps.hyperion.sample;

public class ScreenRecorderActivity extends PluginActivity {

    @Override
    protected int getPluginImage() {
        return R.drawable.screen_recorder;
    }

    @Override
    protected int getPluginTitle() {
        return R.string.screen_recorder;
    }

    @Override
    protected int getPluginDescription() {
        return R.string.screen_recorder_desc;
    }

}