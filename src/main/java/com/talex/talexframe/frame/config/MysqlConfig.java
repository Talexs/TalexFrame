package com.talex.talexframe.frame.config;

import com.talex.talexframe.frame.core.modules.mysql.core.MysqlInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <br /> {@link com.talex.frame.talexframe.config Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:48 <br /> Project: TalexFrame <br />
 */
@Configuration
public class MysqlConfig {

    private static MysqlInfo MYSQL_INFO;

    public static MysqlInfo getInfo() {

        if( MYSQL_INFO == null ) {

            MYSQL_INFO = new MysqlInfo()
                    .setIp(instance.ip).setPort(instance.port)
                    .setDatabaseName(instance.database)
                    .setPassword(instance.password)
                    .setUsername(instance.username)
                    .setUseSSL(instance.useSSL);

        }

        return  MYSQL_INFO;

    }

    private static MysqlConfig instance;

    public MysqlConfig() {

        instance = this;

    }

    @Value("${mysql.ip}")
    private String ip;

    @Value("${mysql.port}")
    private int port;

    @Value("${mysql.database}")
    private String database;

    @Value("${mysql.username}")
    private String username;

    @Value("${mysql.password}")
    private String password;

    @Value("${mysql.useSSL}")
    private boolean useSSL;

}
