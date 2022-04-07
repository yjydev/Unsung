package com.ssafy.unsung.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.ssafy.unsung.dto.KeywordDto;
import com.ssafy.unsung.repository.TopKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.unsung.model.Keyword;
import com.ssafy.unsung.repository.KeywordRepository;

@Service
public class KeywordService {

	@Autowired
	private KeywordRepository keywordRepository;
	
	@Transactional
	public List<TopKeyword> findTopKeyword(){
		return keywordRepository.findTopKeyword();
	}

	@Transactional
	public Keyword saveKeyword(KeywordDto keywordDto){
		Keyword keyword = Keyword.builder()
				.keyword(keywordDto.getKeyword())
				.build();
		return keywordRepository.save(keyword);
	}
}
