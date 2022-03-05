package com.talex.talexframe.frame.core.pojo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加了此注解代表需要登录才可以使用相关功能
 * <br /> {@link com.talex.frame.talexframe.pojo.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 0:19 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( {ElementType.METHOD, ElementType.TYPE} )
public @interface TReqLoginChecker {

    boolean value() default true;

}
