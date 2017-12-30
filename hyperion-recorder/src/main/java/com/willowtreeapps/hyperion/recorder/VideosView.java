package com.willowtreeapps.hyperion.recorder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VideosView extends FrameLayout {

    private static final Executor exec = Executors.newCachedThreadPool();
    private static final Handler main = new Handler(Looper.getMainLooper());

    private RecyclerView recyclerView;

    public VideosView(@NonNull Context context) {
        this(context, null);
    }

    public VideosView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideosView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.hr_view_videos, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final Context context = getContext();
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new VideosAdapter(context));
    }

    private static class VideosAdapter extends RecyclerView.Adapter<VideoViewHolder> {

        private List<File> videoPaths;

        private VideosAdapter(Context context) {
            File file = new File(context.getFilesDir() + "/hyperion_recorder/");
            File[] files = file.listFiles();
            if (files == null) {
                videoPaths = Collections.emptyList();
            } else {
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        return (int) (o2.lastModified() - o1.lastModified());
                    }
                });
                videoPaths = Arrays.asList(files);
            }
        }

        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new VideoViewHolder(inflater.inflate(R.layout.hr_item_video, parent, false));
        }

        @Override
        public void onBindViewHolder(VideoViewHolder holder, int position) {
            holder.bind(videoPaths.get(position));
        }

        @Override
        public int getItemCount() {
            return videoPaths.size();
        }
    }

    private static class VideoViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private final TextView nameText;
        private final TextView dateText;
        private final ImageView thumbnail;
        private File file;

        private VideoViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nameText = itemView.findViewById(R.id.name);
            dateText = itemView.findViewById(R.id.date);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }

        private void bind(final File file) {
            this.file = file;
            nameText.setText(file.getName());
            Date lastModDate = new Date(file.lastModified());
            dateText.setText(lastModDate.toString());
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    final Bitmap thumb = ThumbnailUtils.createVideoThumbnail(file.getPath(),
                            MediaStore.Images.Thumbnails.MINI_KIND);
                    main.post(new Runnable() {
                        @Override
                        public void run() {
                            thumbnail.setAlpha(0.0f);
                            thumbnail.setImageBitmap(thumb);
                            thumbnail.animate().alpha(1.0f).start();
                        }
                    });
                }
            });
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(Intent.ACTION_SEND);
            Uri uri = FileProvider.getUriForFile(
                    context, context.getPackageName() + ".RecorderFileProvider", file);
            intent.setType("video/mp4");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(intent, "Share file with..."));
        }
    }
}