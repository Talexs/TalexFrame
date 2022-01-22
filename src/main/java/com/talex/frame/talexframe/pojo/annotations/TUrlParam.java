package com.talex.frame.talexframe.pojo.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 注入URL字段
 * <br /> {@link com.talex.frame.talexframe.pojo.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/22 17:14 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
public @interface TUrlParam {

    /**
     *
     * 默认解析json字段为当前参数名，设置后将按照field解析
     *
     */
    String field() default "";

}
