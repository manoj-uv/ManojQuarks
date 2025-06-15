package com.example.assessment.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import java.io.IOException;

@Configuration
//@Profile("test")
public class EmbeddedRedisConfig {

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = RedisServer.newRedisServer()
                .port(6379)
                .setting("maxmemory 128M")
                .build();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() throws IOException {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }
}

