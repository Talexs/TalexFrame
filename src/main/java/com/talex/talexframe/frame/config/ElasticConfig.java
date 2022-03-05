package com.talex.talexframe.frame.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <br /> {@link com.talex.frame.talexframe.config Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 1:05 <br /> Project: TalexFrame <br />
 */
@Configuration
@Getter
public class ElasticConfig {

    public static ElasticConfig INSTANCE;

    public ElasticConfig() {

        INSTANCE = this;

    }

    /** 协议 */
    @Value("${elasticsearch.schema:http}")
    private String schema;

    /** 集群地址，如果有多个用“,”隔开 */
    @Value("${elasticsearch.address}")
    private String address;

    /** 连接超时时间 */
    @Value("${elasticsearch.connectTimeout:5000}")
    private int connectTimeout;

    /** Socket 连接超时时间 */
    @Value("${elasticsearch.socketTimeout:10000}")
    private int socketTimeout;

    /** 获取连接的超时时间 */
    @Value("${elasticsearch.connectionRequestTimeout:5000}")
    private int connectionRequestTimeout;

    /** 最大连接数 */
    @Value("${elasticsearch.maxConnectNum:100}")
    private int maxConnectNum;

    /** 最大路由连接数 */
    @Value("${elasticsearch.maxConnectPerRoute:100}")
    private int maxConnectPerRoute;

}
