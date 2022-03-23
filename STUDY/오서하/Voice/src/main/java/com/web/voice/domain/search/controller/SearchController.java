package com.web.voice.domain.search.controller;

import com.web.voice.domain.search.service.SearchService;
import com.web.voice.global.Redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/search")
public class SearchController {
//    @Autowired
//    private SearchService searchService;

    @Autowired
    private RedisService redisService;


    @GetMapping("getValue/{keyword}")
    public void getValue(@PathVariable("keyword") String keyword){
        System.out.println(keyword + "enter!");
        redisService.getRedisStringValue(keyword);
    }
}
