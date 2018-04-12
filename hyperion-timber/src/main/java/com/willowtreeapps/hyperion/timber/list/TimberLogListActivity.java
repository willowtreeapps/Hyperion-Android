package com.willowtreeapps.hyperion.timber.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;
import com.willowtreeapps.hyperion.timber.R;
import com.willowtreeapps.hyperion.timber.model.CircularBuffer;
import com.willowtreeapps.hyperion.timber.model.LogItem;
import com.willowtreeapps.hyperion.timber.timber.TimberLogTree;

import java.util.List;

import timber.log.Timber;

@HyperionIgnore
public class TimberLogListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmb_activity_timber_list);
        setSupportActionBar((Toolbar) findViewById(R.id.tmb_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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

}