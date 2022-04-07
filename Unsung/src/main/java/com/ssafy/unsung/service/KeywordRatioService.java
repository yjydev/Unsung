package com.ssafy.unsung.service;

import com.ssafy.unsung.dto.KeywordRatioDto;
import com.ssafy.unsung.model.KeywordRatio;
import com.ssafy.unsung.repository.KeywordRatioRepository;
import com.ssafy.unsung.repository.WordCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class KeywordRatioService {

    @Autowired
    private KeywordRatioRepository keywordRatioRepository;

    @Transactional
    public List<KeywordRatio> findKeyword(String keyword){
        List<KeywordRatio> newsInfo = keywordRatioRepository.findByKeyword(keyword);
        return newsInfo;
    }

    @Transactional
//    public List<KeywordRatioDto> find(String keyword){
//        List<KeywordRatio> list = keywordRatioRepository.findByKeyword(keyword);
//        List<KeywordRatio> keywordList = new ArrayList<>();
//
//
//        }

    public List<WordCount> wordCount() {return keywordRatioRepository.wordCount();}
}
