package com.dawn.api.router.core;

import android.content.Context;

import com.dawn.annotation.thread.ThreadMode;
import com.dawn.api.router.extra.ActionWrapper;
import com.dawn.api.router.interceptor.ActionInterceptor;
import com.dawn.api.router.interceptor.ActionInterceptorChain;
import com.dawn.api.router.result.ActionCallback;
import com.dawn.api.router.thread.ActionPost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dawn on 2018/9/14.
 */

public class RouterForward {
    private ActionWrapper mActionWrapper;
    private Context mContext;
    private Map<String,Object> mParams;
    private ThreadMode mThreadMode = null;
    //所有拦截器表
    private List<ActionInterceptor> interceptors;

    /**
     * 指定 threadMode 这里指定的优先级高于 Action 注解上的 threadMode
     * @param threadMode
     * @return
     */
    public RouterForward threadMode(ThreadMode threadMode){
        this.mThreadMode = threadMode;
        return this;
    }

    RouterForward(ActionWrapper actionWrapper, List<ActionInterceptor> interceptors){
        this.mActionWrapper = actionWrapper;
        mParams = new HashMap<>();
        this.interceptors = interceptors;
    }

    /**
     * 执行action
     */
    public void invokeAction(){
        invokeAction(ActionCallback.DEFAULT_ACTION_CALLBACK);
    }

    /**
     * 执行 Action
     * @param actionCallback
     */
    public void invokeAction(ActionCallback actionCallback){
        // 先封装 actionPost
        mActionWrapper.setThreadMode(getThreadMode());
        ActionPost actionPost = ActionPost.obtainActionPost(mActionWrapper, mContext, mParams, actionCallback);
        // 开始拦截器的流程
        ActionInterceptor.ActionChain chain = new ActionInterceptorChain(interceptors, actionPost, 0);
        chain.proceed(actionPost);
    }

    /**
     *  路由转发方法传递的 threadMode 优先级高于 Action 注解上的 threadMode
     * @return
     */
    public ThreadMode getThreadMode(){
        return mThreadMode == null ? mActionWrapper.getThreadMode() : mThreadMode;
    }

    public RouterForward context(Context context){
        this.mContext = context;
        return this;
    }

    public RouterForward param(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public RouterForward param(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }
}
