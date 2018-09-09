package com.dawn.core;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

//注册界面注册，数据库的使用，路由，网络请求，缓存，自动生产MVP的P层注册

public final class GS {
    private GS(){}

    public static Application app(){
        if(Build.app == null){
            try{
                // 在IDE进行布局预览时使用
                Class<?> renderActionClass = Class.forName("com.android.layoutlib.bridge.impl.RenderAction");
                Method method = renderActionClass.getDeclaredMethod("getCurrentContext");
                Context context = (Context) method.invoke(null);
                Build.app = new GSApplication(context);
                //

            }catch (Throwable ignored){
                throw new RuntimeException("please register your Application in manifest.");
            }
        }
        return Build.app;
    }

    public static class Build{
        private static boolean debug;
        private static Application app;

        private Build(){}


    }


    private static class GSApplication extends Application{
        public GSApplication(Context baseContext){
            this.attachBaseContext(baseContext);
        }
    }
}
