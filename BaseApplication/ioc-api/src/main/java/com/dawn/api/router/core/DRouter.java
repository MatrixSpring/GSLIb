package com.dawn.api.router.core;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.dawn.api.router.action.IRouterModule;
import com.dawn.api.router.interceptor.ActionInterceptor;
import com.dawn.api.utils.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DRouter {
    //是否被初始化
    private volatile static boolean hasInit = false;
    //是否是 debugable 状态
    private volatile static boolean debuggable = false;
    //日志打印
    public volatile static ILogger logger = new DefaultLogger();
    // 缓存的 RouterAction
    private volatile static Map<String, ActionWrapper> cacheRouterActions = new HashMap();
    //缓存的 RouterModule
    private volatile static Map<String, IRouterModule> cacheRouterModules = new HashMap();
    //所有 module
    private static List<String> mAllModuleClassName;
    private Context mApplicationContext;

    private static List<ActionInterceptor> interceptors = new ArrayList<>();

    public static synchronized void openDebug(){
        debuggable = true;
        logger.showLog(true);
        logger.d(Consts.TAG, "DRouter openDebug");
    }

    private volatile static DRouter instance = null;

    public static boolean isDebuggable(){
        return debuggable;
    }

    private DRouter(){}

    public static DRouter getInstance(){
        if(null == instance){
            synchronized (DRouter.class){
                if(instance == null){
                    instance = new DRouter();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化数据
     * @param context
     */
    public void init(Application context){
        if(hasInit){
            throw new InitException("ARouter already initialized, It can only be initialized once.");
        }
        hasInit = true;

        this.mApplicationContext = context;
        //获取 包下的所有类名信息
        try {
            mAllModuleClassName = ClassUtils.getFileNameByPackageName(context, Consts.ROUTER_MODULE_PACK_NAME);
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String className : mAllModuleClassName){
            logger.d(Consts.TAG, "扫描到: " + className);
        }
        // 添加并且实例化所有拦截器
        scanAddInterceptors(context);
    }

    /**
     * 扫描并且添加拦截器
     * @param context
     */
    private void scanAddInterceptors(final Context context){
            PosterSuppert.g
    }

}
