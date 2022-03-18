package com.talexframe.frame.core.pojo.annotations;

import com.google.common.annotations.Beta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加了此注解将会自动生成 CRUD 模式接口 <br /> {@link com.talexframe.frame.core.pojo.annotations Package }
 *
 * @author TalexDreamSoul
 * 2022/3/18 19:18 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD } )
@Beta
public @interface TRestFul {

    boolean value() default true;

}
