package com.dawn.api.router.interceptor;

import com.dawn.api.router.extra.ErrorActionWrapper;
import com.dawn.api.router.thread.ActionPost;

public class ErrorActionInterceptor implements ActionInterceptor {
    @Override
    public void intercept(ActionChain chain) {
        ActionPost actionPost = chain.action();
        // 拦截错误
        if (actionPost.actionWrapper instanceof ErrorActionWrapper) {
            chain.onInterrupt();
        }

        // 继续分发
        chain.proceed(actionPost);
    }
}
