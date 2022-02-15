package com.talex.frame.talexframe.dao;

import lombok.extern.slf4j.Slf4j;

/**
 * 封装 SpringBoot 事务到框架 -> 插件
 * <br /> {@link com.talex.frame.talexframe.dao Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 14:46 <br /> Project: TalexFrame <br />
 */
// @Component
@Slf4j
@Deprecated
public class MajorDAO {

    public static MajorDAO instance;

    public MajorDAO() {

        instance = this;

    }

}
