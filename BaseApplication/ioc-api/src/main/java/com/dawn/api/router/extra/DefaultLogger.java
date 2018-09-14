package com.dawn.api.router.extra;

import android.text.TextUtils;
import android.util.Log;

public class DefaultLogger implements ILogger {
    boolean isShowLog = false;
    private String defaultTag = "DRouter";

    @Override
    public void showLog(boolean isShowLog) {
        this.isShowLog = isShowLog;
    }

    @Override
    public void d(String tag, String message) {
        if (isShowLog) {
            Log.d(TextUtils.isEmpty(tag) ? getDefaultTag() : tag, message);
        }
    }

    @Override
    public void i(String tag, String message) {
        if (isShowLog) {
            Log.i(TextUtils.isEmpty(tag) ? getDefaultTag() : tag, message);
        }
    }

    @Override
    public void w(String tag, String message) {
        if (isShowLog) {
            Log.w(TextUtils.isEmpty(tag) ? getDefaultTag() : tag, message);
        }
    }

    @Override
    public void e(String tag, String message) {
        if (isShowLog) {
            Log.e(TextUtils.isEmpty(tag) ? getDefaultTag() : tag, message);
        }
    }


    public String getDefaultTag() {
        return defaultTag;
    }
}

