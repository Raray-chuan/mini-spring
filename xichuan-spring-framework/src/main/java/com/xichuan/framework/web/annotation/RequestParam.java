package com.xichuan.framework.web.annotation;

import java.lang.annotation.*;

/**
 * Created by XiChuan on 2022/5/13.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    String value() default "";
}
