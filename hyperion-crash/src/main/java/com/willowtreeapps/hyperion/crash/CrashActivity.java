package com.willowtreeapps.hyperion.crash;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;

@HyperionIgnore
public class CrashActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_REPORT = "hr_report";

    private Report report;

    public static void start(Context context, Report report) {
        final Intent intent = new Intent(context, CrashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_REPORT, report);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hc_activity);
        report = (Report) getIntent().getSerializableExtra(EXTRA_REPORT);

        final FloatingActionButton fab = findViewById(R.id.fab);
        final TextView headerText = findViewById(R.id.header);
        final TextView stacktraceText = findViewById(R.id.stacktrace);
        final View container = findViewById(R.id.container);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_SEND);
                final String text = getExternalText();
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        stacktraceText.setHorizontallyScrolling(true);
        stacktraceText.setMovementMethod(new ScrollingMovementMethod());
        headerText.setText(report.header);
        stacktraceText.setText(report.trace);
        container.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("Hyperion Crash Report", getExternalText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied crash report to clipboard", Toast.LENGTH_LONG).show();
        }
    }

    private String getExternalText() {
        return report.header + "\n\n" + report.trace;
    }
}