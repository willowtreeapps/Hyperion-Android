package com.willowtreeapps.hyperion.core.internal;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

abstract class FragmentManagerCompat {

    static FragmentManagerCompat create(Activity activity) {
        if (activity instanceof AppCompatActivity) {
            return new Support((AppCompatActivity) activity);
        } else {
            return new Android(activity);
        }
    }

    boolean isSupport() {
        return this instanceof Support;
    }

    @Nullable
    abstract <T> T findFragmentByTag(String tag);

    abstract FragmentCompatTransaction beginTransaction();

    private static class Android extends FragmentManagerCompat {

        private final Activity activity;

        private Android(Activity activity) {
            this.activity = activity;
        }

        @Nullable
        @Override
        <T> T findFragmentByTag(String tag) {
            //noinspection unchecked
            return (T) activity.getFragmentManager().findFragmentByTag(tag);
        }

        @SuppressLint("CommitTransaction")
        @Override
        FragmentCompatTransaction beginTransaction() {
            return FragmentCompatTransaction.beginTransaction(
                    activity.getFragmentManager().beginTransaction());
        }
    }

    private static class Support extends FragmentManagerCompat {

        private final AppCompatActivity activity;

        private Support(AppCompatActivity activity) {
            this.activity = activity;
        }

        @Nullable
        @Override
        <T> T findFragmentByTag(String tag) {
            //noinspection unchecked
            return (T) activity.getSupportFragmentManager().findFragmentByTag(tag);
        }

        @SuppressLint("CommitTransaction")
        @Override
        FragmentCompatTransaction beginTransaction() {
            return FragmentCompatTransaction.beginTransaction(
                    activity.getSupportFragmentManager().beginTransaction());
        }
    }

}