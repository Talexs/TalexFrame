package com.talex.frame.talexframe.function.event;

import com.talex.frame.talexframe.pojo.enums.EventPriority;
import com.talex.frame.talexframe.pojo.enums.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author TalexDreamSoul
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TalexSubscribe {

    ThreadMode threadMode() default ThreadMode.POSTING;

    /**
     *
     * 事件优先级
     *
     */
    EventPriority priority() default EventPriority.NORMAL;

}
