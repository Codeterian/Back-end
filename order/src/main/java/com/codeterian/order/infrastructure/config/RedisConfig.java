package com.codeterian.order.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
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

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String redisHost;

	@Value("${spring.data.redis.port}")
	private int redisPort;

	@Value("${spring.data.redis.password}")
	private String redisPassword;

	private static final String REDISSON_HOST_PREFIX = "redis://";

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// ObjectMapper 설정
		ObjectMapper objectMapper = new ObjectMapper();

		// JavaTimeModule 등록: LocalDate, LocalDateTime 등을 직렬화할 수 있게 설정
		objectMapper.registerModule(new JavaTimeModule());

		// 타임스탬프로 직렬화하지 않음 (ISO 8601 형식 사용)
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		// Default Typing 설정: NON_FINAL 타입만 직렬화
		objectMapper.activateDefaultTyping(
				LaissezFaireSubTypeValidator.instance,
				ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.PROPERTY
		);

		// GenericJackson2JsonRedisSerializer로 설정
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

		// Key는 String으로 직렬화
		template.setKeySerializer(new StringRedisSerializer());

		// Value는 JSON으로 직렬화
		template.setValueSerializer(serializer);

		// HashKey, HashValue도 각각 설정 (필요에 따라 설정)
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(serializer);

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

	@Bean
	public RedissonClient redissonClient() {
		RedissonClient redisson = null;
		Config config = new Config();
		config.useSingleServer()
			.setAddress(REDISSON_HOST_PREFIX + redisHost + ":" + redisPort)
			.setPassword(redisPassword);
		redisson = Redisson.create(config);
		return redisson;
	}


}
