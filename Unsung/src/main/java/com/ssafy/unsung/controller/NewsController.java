package com.ssafy.unsung.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.unsung.model.News;
import com.ssafy.unsung.service.NewsService;

@RestController
@RequestMapping("/api/news")
@CrossOrigin("*")
public class NewsController {

	@Autowired
	private NewsService NewsService;
	
	
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<News>> search(@PathVariable("keyword") String keyword){
		return ResponseEntity.ok(NewsService.findKeywords(keyword));
	}
}
