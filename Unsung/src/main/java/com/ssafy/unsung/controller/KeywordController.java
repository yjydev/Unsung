package com.ssafy.unsung.controller;

import java.util.List;

import com.ssafy.unsung.repository.TopKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.unsung.model.Keyword;
import com.ssafy.unsung.service.KeywordService;

@RestController
@RequestMapping("api/keyword")
@CrossOrigin("*")
public class KeywordController {
	
	@Autowired
	private KeywordService keywordService;
	
	@GetMapping("/search")
	public ResponseEntity<List<TopKeyword>> topKeyword(){
		return new ResponseEntity<List<TopKeyword>>(keywordService.findTopKeyword(), HttpStatus.OK);
	}
}
