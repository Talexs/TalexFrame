package com.talex.frame.talexframe.pojo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <br /> {@link com.talex.frame.talexframe.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 10:36 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( {ElementType.PACKAGE, ElementType.METHOD } )
public @interface WrapperResponse {

    /**
     *
     * 返回为假不包装 Response 默认为真
     *
     */
    boolean value() default true;

}
