package com.willowtreeapps.hyperion.crash;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;

@AutoService(Plugin.class)
public class CrashPlugin extends Plugin {

    @Override
    protected void onApplicationCreated(@NonNull Context context) {
        Thread.UncaughtExceptionHandler handler = Thread.currentThread().getUncaughtExceptionHandler();
        if (handler == null) {
            handler = new Handler(context);
        } else {
            handler = new Wrapper(handler, new Handler(context));
        }

        Thread.currentThread().setUncaughtExceptionHandler(handler);
    }

    private static final class Handler implements Thread.UncaughtExceptionHandler {

        private final Context context;
        private final ReportFactory reportFactory;

        private Handler(Context context) {
            this.context = context;
            this.reportFactory = new ReportFactory(context);
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            CrashActivity.start(context, reportFactory.createReport(e));
        }
    }

    private static final class Wrapper implements Thread.UncaughtExceptionHandler {

        private final Thread.UncaughtExceptionHandler base;
        private final Handler handler;

        private Wrapper(Thread.UncaughtExceptionHandler base, Handler handler) {
            this.base = base;
            this.handler = handler;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            handler.uncaughtException(t, e);
            base.uncaughtException(t, e);
        }
    }
}