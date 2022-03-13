package com.talexframe.frame.core.pojo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <br /> {@link com.talexframe.frame.pojo.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 19:38 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface TRequest {

    /**
     * 接口的地址
     */
    String value();

    /**
     * 是否自动解析为 JSON 格式注入
     */
    boolean parseJSON() default true;

}
