package com.ssafy.unsung.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.unsung.model.Keyword;
import com.ssafy.unsung.repository.KeywordRepository;

@Service
public class KeywordService {

	@Autowired
	private KeywordRepository keywordRepository;
	
	@Transactional
	public List<Keyword> findKeywords(String keyword){
		List<Keyword> keywordInfo = keywordRepository.findByKeyword(keyword);
		return keywordInfo;
	}
}
