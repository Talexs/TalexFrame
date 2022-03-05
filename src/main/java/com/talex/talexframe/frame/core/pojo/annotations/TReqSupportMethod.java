package com.talex.talexframe.frame.core.pojo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define a request must be which method sent
 * <br /> {@link com.talex.talexframe.frame.pojo.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 0:45 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface TReqSupportMethod {

    boolean get() default true;
    boolean post() default false;
    boolean put() default false;
    boolean delete() default false;
    boolean head() default false;
    boolean options() default false;
    boolean patch() default false;

}
