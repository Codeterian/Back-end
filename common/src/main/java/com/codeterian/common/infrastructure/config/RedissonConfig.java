// package com.codeterian.common.infrastructure.config;
//
// import org.redisson.Redisson;
// import org.redisson.api.RedissonClient;
// import org.redisson.config.Config;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// @Configuration
// public class RedissonConfig {
//     @Value("${spring.data.redis.host}")
//     private String redisHost;
//
//     @Value("${spring.data.redis.port}")
//     private int redisPort;
//
//     private static final String REDISSON_HOST_PREFIX = "redis://";
//
//     //Redisson 클라이은트를 빈으로 등록
//     @Bean
//     public RedissonClient redissonClient() {
//         RedissonClient redisson = null;
//         Config config = new Config();
//         config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + redisHost + ":" + redisPort);
//         redisson = Redisson.create(config);
//         return redisson;
//     }
// }
