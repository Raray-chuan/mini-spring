package com.xichuan.framework.enums;

/**
 * @Author Xichuan
 * @Date 2022/5/7 11:25
 * @Description
 */

/**
 * Scope的枚举类
 */
public enum ScopeEnum {
    SingleTon("singleton"),ProtoType("prototype");
    private String name;

    private ScopeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
