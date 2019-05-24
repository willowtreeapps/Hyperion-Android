package com.willowtreeapps.hyperion.disk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileExplorerView extends FrameLayout implements Files.Listener {

    private Files files;
    private RecyclerView recycler;
    private Adapter adapter;

    public FileExplorerView(@NonNull Context context) {
        this(context, null);
    }

    public FileExplorerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FileExplorerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.hd_view_file_explorer, this);
        try {
            PackageManager pm = context.getPackageManager();
            String name = context.getPackageName();
            PackageInfo info = pm.getPackageInfo(name, 0);
            String initialPath = info.applicationInfo.dataDir;
            files = new Files(initialPath);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            files = new Files(context.getFilesDir().getPath());
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final Context context = getContext();
        recycler = findViewById(R.id.recycler);
        adapter = new Adapter();
        recycler.setLayoutManager(new LinearLayoutManager(context));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
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
        adapter.list = toItems(files);
        adapter.notifyDataSetChanged();
    }

    private List<FileItem> toItems(List<File> files) {
        List<FileItem> ret = new ArrayList<>(files.size());
        for (File file : files) {
            ret.add(new FileItem(getContext(), file));
        }
        return ret;
    }

    private class Adapter extends RecyclerView.Adapter {

        private List<FileItem> list;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == 1) {
                View itemView = inflater.inflate(R.layout.hd_item_up, parent, false);
                return new UpViewHolder(files, itemView);
            }
            View itemView = inflater.inflate(R.layout.hd_item_file, parent, false);
            return new FileViewHolder(files, itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof FileViewHolder) {
                ((FileViewHolder) holder).bind(list.get(files.atRoot() ? position : position - 1));
            }
        }

        @Override
        public int getItemCount() {
            int count = list == null ? 0 : list.size();
            return files.atRoot() ? count : ++count;
        }

        @Override
        public int getItemViewType(int position) {
            if (!files.atRoot() && position == 0) {
                return 1;
            }
            return super.getItemViewType(position);
        }
    }

    private static class FileViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        private final Files files;
        private final ImageView image;
        private final TextView text;
        private final TextView date;
        private final View options;
        private final int imageSize;
        private FileItem item;

        private FileViewHolder(Files files, final View itemView) {
            super(itemView);
            this.files = files;
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            options = itemView.findViewById(R.id.options);
            options.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupMenu menu = new PopupMenu(itemView.getContext(), options);
                    menu.inflate(R.menu.hd_file_options);
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final Context context = v.getContext();
                            int id = item.getItemId();
                            if (id == R.id.share) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType(FileViewHolder.this.item.mimeType);
                                intent.putExtra(Intent.EXTRA_STREAM, FileViewHolder.this.item.uri);
                                context.startActivity(Intent.createChooser(intent, "Share file"));
                                return true;
                            }
                            if (id == R.id.delete) {
                                boolean deleted = FileViewHolder.this.item.file.delete();
                                if (!deleted) {
                                    Toast.makeText(context, "Could not delete file.", Toast.LENGTH_LONG).show();
                                }
                                return true;
                            }
                            return false;
                        }
                    });
                    menu.show();
                }
            });
            imageSize = (int) itemView.getContext().getResources().getDimension(R.dimen.hd_file_image_size);
        }

        private void bind(FileItem item) {
            this.item = item;
            if (item.isVideo()) {
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(
                        item.path, MediaStore.Video.Thumbnails.MINI_KIND);
                setImageBitmap(thumbnail);
            } else if (item.isImage()) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(item.path, bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true);
                setImageBitmap(bitmap);
            } else if (item.isDirectory) {
                setImageResource(R.drawable.hd_ic_folder);
            } else {
                setImageResource(R.drawable.hd_ic_file);
            }
            text.setText(item.name);
            date.setText(item.lastModified);
        }

        private void setImageResource(@DrawableRes int resId) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageTintMode(PorterDuff.Mode.SRC_IN);
            }
            image.setImageResource(resId);
        }

        private void setImageBitmap(Bitmap bitmap) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageTintMode(PorterDuff.Mode.DST_IN);
            }
            image.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            if (item.isDirectory) {
                files.setPath(item.path);
            }
        }
    }

    private static class UpViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private final Files files;

        private UpViewHolder(Files files, View itemView) {
            super(itemView);
            this.files = files;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            files.pop();
        }
    }
}