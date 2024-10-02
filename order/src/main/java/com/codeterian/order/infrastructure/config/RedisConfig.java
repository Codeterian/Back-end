package com.codeterian.order.infrastructure.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.codeterian.order.domain.entity.order.Orders;

@Configuration
@EnableCaching
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Orders> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Orders> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		return template;
	}

	@Bean
	public RedisCacheManager cacheManager(
		RedisConnectionFactory redisConnectionFactory
	) {
		RedisCacheConfiguration configuration = RedisCacheConfiguration
			.defaultCacheConfig()
			.disableCachingNullValues()
			.entryTtl(Duration.ofSeconds(600))
			.computePrefixWith(CacheKeyPrefix.simple())
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java())
			);
		// redis 캐시이름별로 TTL 다르게 설정
		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

		cacheConfigurations.put("orderCache", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(2)));

		cacheConfigurations.put("orderAllCache", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1)));


		return RedisCacheManager
			.builder(redisConnectionFactory)
			.cacheDefaults(configuration)
			.withInitialCacheConfigurations(cacheConfigurations)
			.build();
	}

}
