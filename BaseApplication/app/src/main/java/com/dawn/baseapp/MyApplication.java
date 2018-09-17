package com.dawn.baseapp;

import android.app.Application;

import com.drouter.api.core.DRouter;

public class MyApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 开启 debug
        DRouter.openDebug();
        // 初始化且只能初始化一次，参数必须是 Application
        DRouter.getInstance().init(this);
    }
}
