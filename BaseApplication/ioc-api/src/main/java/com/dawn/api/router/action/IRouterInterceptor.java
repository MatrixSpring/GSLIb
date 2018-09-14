package com.dawn.api.router.action;

import com.dawn.api.router.interceptor.ActionInterceptor;

import java.util.List;

public interface IRouterInterceptor {
    // 通过 Action 的名称找到 Action
    List<ActionInterceptor> getInterceptors();
}
