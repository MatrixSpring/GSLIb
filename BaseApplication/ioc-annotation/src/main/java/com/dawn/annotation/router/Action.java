package com.dawn.annotation.router;

import com.dawn.annotation.thread.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Action {
    /**
     * thread mode
     * @return
     */
    ThreadMode threadMode() default ThreadMode.POSTING;

    /**
     * path of route
     * @return
     */
    String path();

    /**
     * extra process
     * @return
     */
    boolean extraProcess() default false;
}
