package com.teng.maidada.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/5/30 13:49
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonConfig {
    private String host;

    private Integer port;

    private Integer database;

    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String address = "redis://" + host + ":" + port;
        config.useSingleServer().setAddress(address).setDatabase(database);
        return Redisson.create(config);
    }
}
