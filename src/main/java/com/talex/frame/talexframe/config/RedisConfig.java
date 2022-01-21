package com.talex.frame.talexframe.config;

/**
 * Redis 容器注入
 * <br /> {@link com.talex.frame.talexframe.config Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/15 23:18 <br /> Project: TalexFrame <br />
 */
// @Configuration
    @Deprecated
public class RedisConfig {

    // @Bean
    // @ConditionalOnMissingBean
    // public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    //
    //     RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    //
    //     redisTemplate.setConnectionFactory(connectionFactory);
    //
    //     Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
    //
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     objectMapper.setVisibility(PropertyAccessor.ALL, com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY);
    //     objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    //
    //     jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    //     redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    //     redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    //
    //     RedisSerializer<?> redisSerializer = new StringRedisSerializer();
    //
    //     redisTemplate.setKeySerializer(redisSerializer);
    //     redisTemplate.setHashKeySerializer(redisSerializer);
    //
    //     redisTemplate.afterPropertiesSet();
    //
    //     return redisTemplate;
    //
    // }

}
