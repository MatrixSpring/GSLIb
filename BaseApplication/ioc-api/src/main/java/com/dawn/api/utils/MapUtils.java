package com.dawn.api.utils;

import com.dawn.api.router.interceptor.ActionInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtils {
    public static List<ActionInterceptor> getInterceptorClasses(Map<Integer, ActionInterceptor> map){
        List<ActionInterceptor> list = new ArrayList<>();
        for(Object key:map.keySet()){
            list.add(map.get(key));
        }
        return list;
    }
}
