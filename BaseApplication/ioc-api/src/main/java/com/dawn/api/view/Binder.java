package com.dawn.api.view;

import android.app.Activity;

public class  Binder {
    private  static final  String SUFFIX ="$ViewBinder";
    public static void bind(Activity target){
        Class<?>  clazz = target.getClass();
        String className =clazz.getName()+SUFFIX;
        try {
            Class<?>  binderClass = Class.forName(className);
            ViewBinder rioBind = (ViewBinder) binderClass.newInstance();
            rioBind.bind(target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}