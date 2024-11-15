package com.study.userservice.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

// @Configuration
public class RedissionConfig {

	@Value("${spring.redission.address}")
	private String address;

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer()
			.setAddress(address)
		;
		return Redisson.create(config);
	}
}
