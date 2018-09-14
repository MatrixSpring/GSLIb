package com.dawn.api.router.interceptor;

import com.dawn.api.router.thread.ActionPost;

/**
 * Created by dawn on 2018/9/13.
 */

public interface ActionInterceptor {
    void intercept(ActionChain chain);

    interface ActionChain {
        // 打断拦截
        void onInterrupt();

        // 分发给下一个拦截器
        void proceed(ActionPost actionPost);

        // 获取 ActionPost
        ActionPost action();

        String actionPath();
    }
}
