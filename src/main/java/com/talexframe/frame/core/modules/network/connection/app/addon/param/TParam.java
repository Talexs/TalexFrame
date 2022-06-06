package com.talexframe.frame.core.modules.network.connection.app.addon.param;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注入JSON字段 <br /> {@link com.talexframe.frame.pojo.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 20:33 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.PARAMETER, ElementType.TYPE } )
public @interface TParam {

    /**
     * 是否允许字段不存在 如果不存在且此项为假则会报错并且返回友好提示
     */
    boolean nullable() default false;

    /**
     * 默认解析json字段为当前参数名，设置后将按照field解析
     */
    String value() default "";

}
