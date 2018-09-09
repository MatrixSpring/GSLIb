package com.dawn.core;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ViewInjector {
    void inject(View view);
    void inject(Activity activity);
    void inject(Object handler, View view);
    View inject(Object fragment, LayoutInflater inflater, ViewGroup container);
}
