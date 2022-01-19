package com.talex.frame.talexframe.pojo.enums;

import lombok.Getter;

public enum EventPriority {

    /** 最先 **/
    HIGHEST(1000),

    /** 优先 **/
    HIGH(100),

    /** 默认 **/
    NORMAL(10),

    /** 较低 **/
    LOW(0),

    /** 最低 **/
    LOWEST(-10)
    ;

    @Getter
    private final int level;

    EventPriority(int level) {

        this.level = level;

    }

}
