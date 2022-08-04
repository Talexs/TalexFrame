package com.talexframe.frame.core.modules.network.connection.app.addon.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加了此注解代表需要登录才可以使用相关功能 <br /> {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul
 * 2022/6/6 05:00 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD } )
public @interface TReqPermissionChecker {

    /**
     * 需要的权限表
     */
    String[] value();

}
