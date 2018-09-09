package com.dawn.core.viewinject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dawn.core.ViewInjector;

public class ViewInjectorImpl implements ViewInjector {


    @Override
    public void inject(View view) {

    }

    @Override
    public void inject(Activity activity) {

    }

    @Override
    public void inject(Object handler, View view) {

    }

    @Override
    public View inject(Object fragment, LayoutInflater inflater, ViewGroup container) {
        return null;
    }
}
