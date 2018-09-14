package com.dawn.api.router.action;

import android.content.Context;

import com.dawn.api.router.result.RouterResult;

import java.util.Map;

/**
 * Created by dawn on 2018/9/13.
 */

public interface IRouterAction {
    //执行action方法
    RouterResult invokeAction(Context context, Map<String, Object> requestData);
}
