package com.willowtreeapps.hyperion.core.internal;

import android.annotation.SuppressLint;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

@SuppressLint("CommitTransaction")
abstract class FragmentCompatTransaction {

    abstract FragmentCompatTransaction add(Object fragment, @Nullable String tag);

    abstract FragmentCompatTransaction add(@IdRes int containerViewId, Object fragment, @Nullable String tag);

    abstract FragmentCompatTransaction remove(Object fragment);

    abstract void commit();

    static FragmentCompatTransaction beginTransaction(android.app.FragmentTransaction transaction) {
        return new Android(transaction);
    }

    static FragmentCompatTransaction beginTransaction(FragmentTransaction transaction) {
        return new Support(transaction);
    }

    private static class Android extends FragmentCompatTransaction {

        private final android.app.FragmentTransaction transaction;

        private Android(android.app.FragmentTransaction transaction) {
            this.transaction = transaction;
        }

        @Override
        FragmentCompatTransaction add(Object fragment, @Nullable String tag) {
            transaction.add((android.app.Fragment) fragment, tag);
            return this;
        }

        @Override
        FragmentCompatTransaction add(int containerViewId, Object fragment, @Nullable String tag) {
            transaction.add(containerViewId, (android.app.Fragment) fragment, tag);
            return this;
        }

        @Override
        FragmentCompatTransaction remove(Object fragment) {
            transaction.remove((android.app.Fragment) fragment);
            return this;
        }

        @Override
        void commit() {
            transaction.commit();
        }
    }

    private static class Support extends FragmentCompatTransaction {

        private final FragmentTransaction transaction;

        private Support(FragmentTransaction transaction) {
            this.transaction = transaction;
        }

        @Override
        FragmentCompatTransaction add(Object fragment, @Nullable String tag) {
            transaction.add((Fragment) fragment, tag);
            return this;
        }

        @Override
        FragmentCompatTransaction add(int containerViewId, Object fragment, @Nullable String tag) {
            transaction.add(containerViewId, (Fragment) fragment, tag);
            return this;
        }

        @Override
        FragmentCompatTransaction remove(Object fragment) {
            transaction.remove((Fragment) fragment);
            return this;
        }

        @Override
        void commit() {
            transaction.commit();
        }
    }

}