package com.web.voice.domain.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RestController
public class RedisController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //  Spring Boot 는 RedisTemplate 라는 추상화를 제공하고 @Resource 을 이용해 redisTemplate 라는 이름의 객체를 주입받는 것이 기본적인 형태
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations; //Redis sortedset

    private String KEY = "fruit";

    @PostMapping("/redisTest")
    public ResponseEntity<?> addRedisKey() {

        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        vop.set("yellow", "banana");
        vop.set("red", "apple");
        vop.set("green", "watermelon");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/redisTest/{key}")
    public ResponseEntity<?> getRedisKey(@PathVariable String key) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String value = vop.get(key);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }
    @GetMapping("/redisTest/top5keyword")
    public ResponseEntity<?>getTopKeyword(){
        Set<String> range = zSetOperations.reverseRange(KEY, 0, 4);
        return new ResponseEntity<>(range, HttpStatus.OK);
    }

}
