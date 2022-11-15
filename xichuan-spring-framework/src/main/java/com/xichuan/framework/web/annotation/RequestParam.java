package com.xichuan.framework.web.annotation;

import java.lang.annotation.*;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    String value() default "";
}
