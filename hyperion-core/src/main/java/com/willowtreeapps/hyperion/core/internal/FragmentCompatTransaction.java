package com.willowtreeapps.hyperion.core.internal;

import android.annotation.SuppressLint;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

@SuppressLint("CommitTransaction")
abstract class FragmentCompatTransaction {

    abstract FragmentCompatTransaction add(Object fragment, @Nullable String tag);

    abstract FragmentCompatTransaction add(@IdRes int containerViewId, Object fragment, @Nullable String tag);

    abstract FragmentCompatTransaction remove(Object fragment);

    abstract void commit();

    static FragmentCompatTransaction beginTransaction(android.app.FragmentTransaction transaction) {
        return new Android(transaction);
    }

    static FragmentCompatTransaction beginTransaction(android.support.v4.app.FragmentTransaction transaction) {
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

        private final android.support.v4.app.FragmentTransaction transaction;

        private Support(android.support.v4.app.FragmentTransaction transaction) {
            this.transaction = transaction;
        }

        @Override
        FragmentCompatTransaction add(Object fragment, @Nullable String tag) {
            transaction.add((android.support.v4.app.Fragment) fragment, tag);
            return this;
        }

        @Override
        FragmentCompatTransaction add(int containerViewId, Object fragment, @Nullable String tag) {
            transaction.add(containerViewId, (android.support.v4.app.Fragment) fragment, tag);
            return this;
        }

        @Override
        FragmentCompatTransaction remove(Object fragment) {
            transaction.remove((android.support.v4.app.Fragment) fragment);
            return this;
        }

        @Override
        void commit() {
            transaction.commit();
        }
    }

}