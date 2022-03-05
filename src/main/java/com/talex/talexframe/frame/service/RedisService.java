package com.talex.talexframe.frame.service;

import org.springframework.stereotype.Service;

/**
 * 针对 ID自增序列 工具类
 * <br /> {@link com.talex.talexframe.frame.service Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/15 23:24 <br /> Project: TalexFrame <br />
 */
@Service
public class RedisService {

    // @Autowired
    // RedisTemplate<String, Object> redisTemplate;

    // /**
    //  *
    //  * 获取连接工厂
    //  *
    //  * @return {@link org.springframework.data.redis.connection.RedisConnectionFactory }
    //  */
    // public RedisConnectionFactory getConnectionFactory() {
    //
    //     return redisTemplate.getConnectionFactory();
    //
    // }
    //
    // /**
    //  *
    //  * 自增数
    //  *
    //  * @param key 类型数据
    //  *
    //  */
    // public long increment(String key) {
    //
    //     RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key, getConnectionFactory());
    //
    //     return redisAtomicLong.incrementAndGet();
    //
    // }
    //
    // /**
    //  *
    //  * 自增数
    //  *
    //  * @param key 类型数据
    //  * @param time 过期时间
    //  * @param timeUnit 时间单位
    //  *
    //  */
    // public long increment(String key, long time, TimeUnit timeUnit) {
    //
    //     RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key, getConnectionFactory());
    //     redisAtomicLong.expire(time, timeUnit);
    //
    //     return redisAtomicLong.incrementAndGet();
    //
    // }
    //
    // /**
    //  * 自增数
    //  *
    //  * @param key 类型数据
    //  * @param expireAt 国企时间
    //  *
    //  */
    // public long increment(String key, Instant expireAt) {
    //
    //     RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key, getConnectionFactory());
    //     redisAtomicLong.expireAt(expireAt);
    //
    //     return redisAtomicLong.incrementAndGet();
    //
    // }

}
