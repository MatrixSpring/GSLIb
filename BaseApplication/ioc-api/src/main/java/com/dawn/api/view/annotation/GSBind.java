package com.dawn.api.view.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//作用域
@Target(ElementType.FIELD)
//保留策略（SOURCE,CLASS,RUNTIME）
@Retention(RetentionPolicy.CLASS)
public @interface GSBind {
    //注解中的值为int类型
    int value();
}
