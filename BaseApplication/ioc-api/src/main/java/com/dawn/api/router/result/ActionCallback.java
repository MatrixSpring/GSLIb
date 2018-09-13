package com.dawn.api.router.result;

/**
 * Created by dawn on 2018/9/13.
 */

public interface ActionCallback {
    /**
     * 被拦截了
     */
    void onInterrupt();

    /**
     * 返回结果
     * @param result
     */
    void onResult(RouterResult result);

    ActionCallback DEFAULT_ACTION_CALLBACK = new ActionCallback() {
        @Override
        public void onInterrupt() {

        }

        @Override
        public void onResult(RouterResult result) {

        }
    };
}
