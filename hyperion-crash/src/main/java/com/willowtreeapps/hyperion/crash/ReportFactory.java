package com.willowtreeapps.hyperion.crash;

import android.content.Context;
import android.os.Build;

class ReportFactory {

    private final Context context;

    ReportFactory(Context context) {
        this.context = context;
    }

    Report createReport(Throwable error) {
        final Report report = new Report();
        report.header = getHeaderText();
        report.trace = createStackTraceString(error);
        return report;
    }

    private String getHeaderText() {
        final StringBuilder sb = new StringBuilder();

        sb.append(createKeyValueLine("Application", context.getPackageName()));
        sb.append(createKeyValueLine("Manufacturer", Build.MANUFACTURER));
        sb.append(createKeyValueLine("Model", Build.MODEL));
        sb.append(createKeyValueLine("Brand", Build.BRAND));
        sb.append(createKeyValueLine("Device", Build.DEVICE));
        sb.append(createKeyValueLine("Board", Build.BOARD));
        sb.append(createKeyValueLine("Hardware", Build.HARDWARE));
        sb.append(createKeyValueLine("Product", Build.PRODUCT));
        sb.append(createKeyValueLine("Android Version", Build.VERSION.RELEASE));

        return sb.toString();
    }

    private String createStackTraceString(Throwable error) {
        final StringBuilder sb = new StringBuilder();
        sb.append(error.getMessage());
        sb.append("\n");
        for (StackTraceElement e : error.getStackTrace()) {
            sb.append(e.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String createKeyValueLine(String key, String value) {
        return key + ": " + value + "\n";
    }
}