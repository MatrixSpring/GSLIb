package com.dawn.api.router.thread;

import com.dawn.api.router.action.IRouterAction;
import com.dawn.api.router.extra.ActionWrapper;
import com.dawn.api.router.result.RouterResult;

public class AsyncPoster implements Runnable, Poster {

    private final ActionPostQueue queue;

    AsyncPoster() {
        queue = new ActionPostQueue();
    }

    @Override
    public void run() {
        ActionPost actionPost = queue.poll();
        if (actionPost == null) {
            throw new IllegalStateException("No pending post available");
        }

        ActionWrapper actionWrapper = actionPost.actionWrapper;
        IRouterAction routerAction = actionWrapper.getRouterAction();
        RouterResult routerResult = routerAction.invokeAction(actionPost.context, actionPost.params);
        actionPost.actionCallback.onResult(routerResult);

        actionPost.releasePendingPost();
    }

    @Override
    public void enqueue(ActionPost actionPost) {
        queue.enqueue(actionPost);
        PosterSupport.getExecutorService().execute(this);
    }
}
