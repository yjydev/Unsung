package com.ssafy.unsung.controller;

import com.ssafy.unsung.dto.NewNewsDto;
import com.ssafy.unsung.service.NewNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/newnews")
@CrossOrigin("*")
public class NewNewsController {

    @Autowired
    private NewNewsService newNewsService;

    @GetMapping("/news")
    public ResponseEntity<List<NewNewsDto>> newNews() throws IOException {
        return new ResponseEntity<List<NewNewsDto>>(newNewsService.crawling(), HttpStatus.OK);
    }

    @GetMapping("/news/{keyword}")
    public ResponseEntity<List<NewNewsDto>> keywordNews(@PathVariable("keyword")String keyword) throws IOException{
        return ResponseEntity.ok(newNewsService.crawling(keyword));
    }
}