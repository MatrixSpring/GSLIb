package com.dawn.api.router.action;

public interface IRouterModule {
    // 通过 Action 的名称找到 Action
    ActionWrapper findAction(String actionName);
}
