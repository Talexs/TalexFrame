package com.talexframe.frame.core.pojo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加在字段上进行请求校验
 * {@link com.talexframe.frame.core.pojo.annotations Package }
 *
 * @author TalexDreamSoul 22/04/17 上午 08:34 Project: TalexFrame
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.PARAMETER, ElementType.TYPE } )
public @interface TPParamValidator {

    /**
     * 校验规则 # 必须满足当前正则表达式
     */
    String pattern() default "";

    /**
     * 校验规则 # 必须满足不为 null
     */
    boolean notNull() default true;

    /**
     * 自动类型断言 # true
     * 必须元素为true可用
     */
    boolean assertTrue() default false;

    /**
     * 自动类型断言 # false
     * 必须元素为false可用
     */
    boolean assertFalse() default false;

    /**
     * 校验规则 # 必须满足最小值
     */
    int min() default Integer.MIN_VALUE;

    /**
     * 校验规则 # 必须满足最大值
     */
    int max() default Integer.MAX_VALUE;

    /**
     * 校验规则 # 必须满足最小长度
     */
    int minLength() default Integer.MIN_VALUE;

    /**
     * 校验规则 # 必须满足最大长度
     */
    int maxLength() default Integer.MAX_VALUE;

}
