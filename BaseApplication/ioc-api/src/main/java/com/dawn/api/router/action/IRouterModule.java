package com.dawn.api.router.action;

import com.dawn.api.router.extra.ActionWrapper;

public interface IRouterModule {
    // 通过 Action 的名称找到 Action
    ActionWrapper findAction(String actionName);
}
