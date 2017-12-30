package com.willowtreeapps.hyperion.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VersionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_version, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recycler = view.findViewById(R.id.recycler);
        recycler.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        recycler.setAdapter(new VersionAdapter());
    }

    private static final class VersionAdapter extends RecyclerView.Adapter<VersionAdapter.VersionViewHolder> {

        private static final List<PluginInfo> list;

        static {
            list = new ArrayList<>(5);
            list.add(new PluginInfo(R.drawable.ruler, R.string.measurement_tool, MeasurementToolActivity.class));
            list.add(new PluginInfo(R.drawable.attribute_inspector, R.string.attributes_inspector, AttributesInspectorActivity.class));
            list.add(new PluginInfo(R.drawable.inactive, R.string.screen_recorder, ScreenRecorderActivity.class));
            list.add(new PluginInfo(R.drawable.folder_2, R.string.file_explorer, FileExplorerActivity.class));
            list.add(new PluginInfo(R.drawable.add, R.string.add_or_remove, null));
        }

        @Override
        public VersionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new VersionViewHolder(inflater.inflate(R.layout.item_plugin_info, parent, false));
        }

        @Override
        public void onBindViewHolder(VersionViewHolder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static final class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private final ImageView image;
            private final TextView text;

            private PluginInfo item;

            public VersionViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.plugin_image);
                text = itemView.findViewById(R.id.plugin_text);
                itemView.setOnClickListener(this);
            }

            private void bind(PluginInfo item) {
                this.item = item;
                image.setImageResource(item.getImage());
                text.setText(item.getTitle());
            }

            @Override
            public void onClick(View v) {
                if (item.activity != null) {
                    final Context context = v.getContext();
                    final Intent intent = new Intent(context, item.activity);
                    context.startActivity(intent);
                }
            }
        }

    }

    private static final class PluginInfo {

        private final @DrawableRes int image;
        private final @StringRes int title;
        private final Class<? extends Activity> activity;

        public PluginInfo(int image, int title, Class<? extends Activity> activity) {
            this.image = image;
            this.title = title;
            this.activity = activity;
        }

        @DrawableRes
        public int getImage() {
            return image;
        }

        @StringRes
        public int getTitle() {
            return title;
        }

        public Class<? extends Activity> getActivity() {
            return activity;
        }
    }
}