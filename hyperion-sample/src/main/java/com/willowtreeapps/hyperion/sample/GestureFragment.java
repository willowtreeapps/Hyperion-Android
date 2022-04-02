package com.willowtreeapps.hyperion.sample;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.willowtreeapps.hyperion.core.Hyperion;

public class GestureFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gesture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final Button openMenuButton = view.findViewById(R.id.open_button);
        openMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hyperion.open(getActivity());
            }
        });

        final Button openCustomMenuButton = view.findViewById(R.id.open_custom_button);
        openCustomMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), FactoryActivity.class));
            }
        });
    }
}