package com.dawn.api.router.extra;

public interface ILogger {
    void showLog(boolean isShowLog);

    void d(String tag, String message);

    void i(String tag, String message);

    void w(String tag, String message);

    void e(String tag, String message);
}
