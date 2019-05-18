package com.willowtreeapps.hyperion.timber.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;
import com.willowtreeapps.hyperion.timber.R;
import com.willowtreeapps.hyperion.timber.model.CircularBuffer;
import com.willowtreeapps.hyperion.timber.model.LogItem;
import com.willowtreeapps.hyperion.timber.timber.TimberLogTree;

import java.util.List;

import timber.log.Timber;

@HyperionIgnore
public class TimberLogListActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar mToolbar;
    Toolbar mFilterToolbar;
    EditText mFilterEditText;
    Button mShareButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmb_activity_timber_list);
        mToolbar = findViewById(R.id.tmb_toolbar);
        mToolbar.inflateMenu(R.menu.tmb_filter);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_item_filter) {
                    fadeIn(mFilterToolbar);
                    return true;
                }
                return false;
            }
        });
        mFilterToolbar = findViewById(R.id.tmb_filter_toolbar);
        mFilterEditText = findViewById(R.id.tmb_filter);

        CircularBuffer<LogItem> logItemQueue = getLogItemQueue();
        final TimberLogListAdapter adapter = new TimberLogListAdapter(logItemQueue);
        mFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.applyFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mFilterToolbar.setNavigationIcon(R.drawable.tmb_ic_close);
        mFilterToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeOut(mFilterToolbar);
                mFilterEditText.getText().clear();
                adapter.applyFilter(null);
            }
        });

        mShareButton = findViewById(R.id.button_share);
        mShareButton.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.tmb_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private CircularBuffer<LogItem> getLogItemQueue() {
        List<Timber.Tree> forest = Timber.forest();
        for (Timber.Tree tree : forest) {
            if (tree instanceof TimberLogTree) {
                return ((TimberLogTree) tree).getCircularBuffer();
            }
        }

        throw new RuntimeException("TimberLogTree not planted in forest.");
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.button_share) {
            collectLogs();
        }
    }

    private void collectLogs() {
        CircularBuffer<LogItem> logItemQueue = getLogItemQueue();
        int size = logItemQueue.size();
        StringBuilder logs = new StringBuilder();
        for (int i = 0; i < size; i++) {
            LogItem logItem = logItemQueue.getItem(i);
            logs.append(logItem.level)
                    .append(" : [")
                    .append(logItem.date)
                    .append("] --> ")
                    .append(logItem.message).append("\n");
        }
        ShareCompat.IntentBuilder
                .from(this)
                .setText(logs.toString())
                // most general text sharing MIME type
                .setType("text/plain")
                .startChooser();
    }

    private void fadeIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        view.animate()
                .alpha(1.0f);
    }

    private void fadeOut(final View view) {
        long duration = 250L;
        view.animate()
                .setDuration(duration)
                .alpha(0.0f);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, duration);
    }
}
