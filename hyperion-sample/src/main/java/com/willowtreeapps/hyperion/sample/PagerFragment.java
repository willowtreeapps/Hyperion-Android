package com.willowtreeapps.hyperion.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PagerFragment extends Fragment {

    private ViewPager pager;
    private MainAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pager = view.findViewById(R.id.pager);
        pager.setAdapter(adapter = new MainAdapter(getFragmentManager()));
    }

    private static final class MainAdapter extends FragmentStatePagerAdapter {

        private MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
                    return new GestureFragment();
                case 2:
                    return new VersionFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}