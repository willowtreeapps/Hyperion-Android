package com.willowtreeapps.hyperion.disk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.willowtreeapps.hyperion.core.DrawerView;
import com.willowtreeapps.hyperion.core.HyperionCore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DiskView extends DrawerView implements Files.Listener {

    @Inject
    Files files;

    private final RecyclerView recycler;
    private final Adapter adapter;

    public DiskView(@NonNull Context context) {
        super(context);
        HyperionCore.<DiskComponent>getComponent(context).inject(this);
        inflate(context, R.layout.view_disk, this);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        adapter = new Adapter();
        recycler.setLayoutManager(new GridLayoutManager(context, 2));
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        files.addListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        files.removeListener(this);
    }

    @Override
    public void onFilesChanged(List<File> files) {
        adapter.files = toItems(files);
        adapter.notifyDataSetChanged();
    }

    private List<FileItem> toItems(List<File> files) {
        List<FileItem> ret = new ArrayList<>(files.size());
        for (File file : files) {
            ret.add(new FileItem(getContext(), file));
        }
        return ret;
    }

    private static class Adapter extends RecyclerView.Adapter<FileViewHolder> {

        private List<FileItem> files;

        @Override
        public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_file, parent, false);
            return new FileViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FileViewHolder holder, int position) {
            holder.bind(files.get(position));
        }

        @Override
        public int getItemCount() {
            return files == null ? 0 : files.size();
        }
    }

    private static class FileViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        private final ImageView image;
        private final TextView text;
        private FileItem item;

        private FileViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        private void bind(FileItem item) {
            this.item = item;
            if (item.isImage()) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(item.path, bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, itemView.getWidth(), itemView.getHeight(), true);
                image.setImageBitmap(bitmap);
            } else if (item.isDirectory) {
                image.setImageResource(R.drawable.ic_folder);
            } else {
                image.setImageResource(R.drawable.ic_file);
            }
            text.setText(item.name + ", " + item.size + " bytes");
        }

        @Override
        public void onClick(View v) {
            if (item.isDirectory) {

            } else {
                final Context context = v.getContext();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType(item.mimeType);
                intent.putExtra(Intent.EXTRA_STREAM, item.uri);
                context.startActivity(Intent.createChooser(intent, "Share file with..."));
            }
        }
    }
}