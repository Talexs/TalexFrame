package com.talex.talexframe.frame.core.modules.mysql.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <br /> {@link com.talex.frame.talexframe.function.mysql.core Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:31 <br /> Project: TalexFrame <br />
 */
@Getter
@Setter
@Accessors( chain = true )
public class MysqlInfo {

    private String ip;
    private String databaseName;
    private String username;
    private String password;
    private int port;
    private boolean useSSL = false;

}
