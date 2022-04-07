package com.ssafy.unsung.controller;

import com.ssafy.unsung.dto.NewsDto;
import com.ssafy.unsung.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/newnews")
@CrossOrigin("*")
public class NewsController {

    @Autowired
    private NewsService newNewsService;

    @GetMapping("/news")
    public ResponseEntity<List<NewsDto>> newNews() throws IOException {
        return new ResponseEntity<List<NewsDto>>(newNewsService.crawling(), HttpStatus.OK);
    }

    @GetMapping("/news/{keyword}")
    public ResponseEntity<List<NewsDto>> keywordNews(@PathVariable("keyword")String keyword) throws IOException{
        return ResponseEntity.ok(newNewsService.crawling(keyword));
    }
}