package com.dawn.api.router.interceptor;

import com.dawn.api.router.thread.ActionPost;

import java.util.List;

public class ActionInterceptorChain implements ActionInterceptor.ActionChain {
    // 是否被拦截了
    private boolean isInterrupt = false;
    private List<ActionInterceptor> interceptors;
    private ActionPost actionPost;
    private int index;

    public ActionInterceptorChain(List<ActionInterceptor> interceptors, ActionPost actionPost, int index) {
        this.interceptors = interceptors;
        this.actionPost = actionPost;
        this.index = index;
    }

    @Override
    public void onInterrupt() {
        isInterrupt = true;
        actionPost.actionCallback.onInterrupt();
    }

    @Override
    public void proceed(ActionPost actionPost) { // 0
        if (!isInterrupt && index < interceptors.size()) {
            // 继续往下分发
            ActionInterceptor.ActionChain next = new ActionInterceptorChain(interceptors, actionPost, index + 1);
            // 0 拦截器
            ActionInterceptor interceptor = interceptors.get(index);
            // 执行第一个
            interceptor.intercept(next);
        }
    }

    @Override
    public ActionPost action() {
        return actionPost;
    }

    @Override
    public String actionPath() {
        return actionPost.actionWrapper.getPath();
    }
}