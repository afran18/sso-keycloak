package com.afran.keycloak_bff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisTestController {

    private final StringRedisTemplate redisTemplate;

    @GetMapping("/redis-test")
    public String test() {

        redisTemplate.opsForValue()
                .set("hello", "afran");

        return redisTemplate.opsForValue()
                .get("hello");
    }
}
