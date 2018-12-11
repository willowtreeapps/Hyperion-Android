package com.willowtreeapps.hyperion.timber.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;
import com.willowtreeapps.hyperion.timber.R;
import com.willowtreeapps.hyperion.timber.model.CircularBuffer;
import com.willowtreeapps.hyperion.timber.model.LogItem;
import com.willowtreeapps.hyperion.timber.timber.TimberLogTree;

import java.util.List;

import timber.log.Timber;

@HyperionIgnore
public class TimberLogListActivity extends AppCompatActivity implements View.OnClickListener {
    Button mShareButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmb_activity_timber_list);
        setSupportActionBar((Toolbar) findViewById(R.id.tmb_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mShareButton = findViewById(R.id.button_share);
        mShareButton.setOnClickListener(this);

        CircularBuffer<LogItem> logItemQueue = getLogItemQueue();
        RecyclerView recyclerView = findViewById(R.id.tmb_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TimberLogListAdapter(logItemQueue));
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
}
