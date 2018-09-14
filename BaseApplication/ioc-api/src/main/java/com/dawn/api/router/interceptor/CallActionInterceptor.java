package com.dawn.api.router.interceptor;

import android.os.Looper;

import com.dawn.api.router.action.IRouterAction;
import com.dawn.api.router.extra.ActionWrapper;
import com.dawn.api.router.result.RouterResult;
import com.dawn.api.router.thread.ActionPost;
import com.dawn.api.router.thread.PosterSupport;

public class CallActionInterceptor implements ActionInterceptor {
    @Override
    public void intercept(ActionChain chain) {
        // 执行 Action 方法
        ActionPost actionPost = chain.action();
        invokeAction(actionPost, Looper.myLooper() == Looper.getMainLooper());
    }

    /**
     * 处理线程切换
     *
     * @param isMainThread
     * @return
     */
    private void invokeAction(ActionPost actionPost, boolean isMainThread) {
        switch (actionPost.actionWrapper.getThreadMode()) {
            case POSTING:
                invokeAction(actionPost);
            case MAIN:
                if (isMainThread) {
                    invokeAction(actionPost);
                } else {
                    PosterSupport.getMainPoster().enqueue(actionPost);
                }
                break;
            case BACKGROUND:
                if (isMainThread) {
                    PosterSupport.getBackgroundPoster().enqueue(actionPost);
                } else {
                    invokeAction(actionPost);
                }
                break;
            case ASYNC:
                PosterSupport.getAsyncPoster().enqueue(actionPost);
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + actionPost.actionWrapper.getThreadMode());
        }
    }

    /**
     * 执行 Action
     *
     * @param actionPost
     */
    private void invokeAction(ActionPost actionPost) {
        ActionWrapper actionWrapper = actionPost.actionWrapper;
        IRouterAction routerAction = actionWrapper.getRouterAction();
        RouterResult routerResult = routerAction.invokeAction(actionPost.context, actionPost.params);
        actionPost.actionCallback.onResult(routerResult);
    }
}