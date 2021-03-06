package com.dawn.api.router.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import com.dawn.api.router.action.IRouterAction;
import com.dawn.api.router.extra.ActionWrapper;
import com.dawn.api.router.result.RouterResult;

public class HandlerPoster extends Handler implements Poster {
    private final ActionPostQueue queue;
    private final int maxMillisInsideHandleMessage;
    private boolean handlerActive;

    protected HandlerPoster(Looper looper, int maxMillisInsideHandleMessage) {
        super(looper);
        this.maxMillisInsideHandleMessage = maxMillisInsideHandleMessage;
        queue = new ActionPostQueue();
    }

    @Override
    public void handleMessage(Message msg) {
        boolean rescheduled = false;
        try {
            long started = SystemClock.uptimeMillis();
            while (true) {
                ActionPost actionPost = queue.poll();
                if (actionPost == null) {
                    synchronized (this) {
                        // Check again, this time in synchronized
                        actionPost = queue.poll();
                        if (actionPost == null) {
                            handlerActive = false;
                            return;
                        }
                    }
                }

                ActionWrapper actionWrapper = actionPost.actionWrapper;
                IRouterAction routerAction = actionWrapper.getRouterAction();
                RouterResult routerResult = routerAction.invokeAction(actionPost.context, actionPost.params);
                actionPost.actionCallback.onResult(routerResult);

                actionPost.releasePendingPost();

                long timeInMethod = SystemClock.uptimeMillis() - started;
                if (timeInMethod >= maxMillisInsideHandleMessage) {
                    if (!sendMessage(obtainMessage())) {
                        throw new RuntimeException("Could not send handler message");
                    }
                    rescheduled = true;
                    return;
                }
            }
        } finally {
            handlerActive = rescheduled;
        }
    }

    @Override
    public void enqueue(ActionPost actionPost) {
        synchronized (this) {
            queue.enqueue(actionPost);
            if (!handlerActive) {
                handlerActive = true;
                if (!sendMessage(obtainMessage())) {
                    throw new RuntimeException("Could not send handler message");
                }
            }
        }
    }
}

