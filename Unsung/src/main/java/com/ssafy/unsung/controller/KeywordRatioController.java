package com.ssafy.unsung.controller;

import com.ssafy.unsung.model.KeywordRatio;
import com.ssafy.unsung.repository.KeywordGraph1;
import com.ssafy.unsung.repository.KeywordGraph2;
import com.ssafy.unsung.repository.KeywordPercent;
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

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<KeywordPercent>> search(@PathVariable("keyword") String keyword){
        return ResponseEntity.ok(keywordRatioService.findKeyword(keyword));
    }

    @GetMapping("/search/graph/{keyword}")
    public ResponseEntity<List<KeywordGraph2>> graph2(@PathVariable("keyword") String keyword){
        return ResponseEntity.ok(keywordRatioService.graph2(keyword));
    }

    @GetMapping("/search/graph/{keyword}/{press}")
    public ResponseEntity<List<KeywordGraph1>> graph1(@PathVariable("keyword") String keyword, @PathVariable("press") String press){
        return ResponseEntity.ok(keywordRatioService.graph1(keyword, press));
    }

    @GetMapping("/search/wordcount")
    public ResponseEntity<List<WordCount>> wordCount(){
        return new ResponseEntity<List<WordCount>>(keywordRatioService.wordCount(), HttpStatus.OK);
    }
}
