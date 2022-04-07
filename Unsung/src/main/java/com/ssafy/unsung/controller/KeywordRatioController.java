package com.ssafy.unsung.controller;

import com.ssafy.unsung.model.KeywordRatio;
import com.ssafy.unsung.repository.WordCount;
import com.ssafy.unsung.service.KeywordRatioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keywordratio")
@CrossOrigin("*")
public class KeywordRatioController {

    @Autowired
    private KeywordRatioService keywordRatioService;

    @GetMapping("search/{keyword}")
    public ResponseEntity<List<KeywordRatio>> search(@PathVariable("keyword") String keyword){
        return ResponseEntity.ok(keywordRatioService.findKeyword(keyword));
    }

    @GetMapping("/search/wordcount")
    public ResponseEntity<List<WordCount>> wordCount(){
        return new ResponseEntity<List<WordCount>>(keywordRatioService.wordCount(), HttpStatus.OK);
    }
}
