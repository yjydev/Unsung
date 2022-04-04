package com.ssafy.unsung.service;

import java.util.List;

import javax.transaction.Transactional;

import com.ssafy.unsung.repository.WordCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.unsung.model.News;
import com.ssafy.unsung.repository.NewsRepository;

@Service
public class NewsService {

	@Autowired
	private NewsRepository newsRepository;
	
	@Transactional
	public List<News> findKeywords(String keyword){
		List<News> NewsInfo = newsRepository.findByKeyword(keyword);
		return NewsInfo;
	}

	public List<WordCount> wordCount() {return newsRepository.wordCount();}
}
