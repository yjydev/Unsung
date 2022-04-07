package com.ssafy.unsung.service;

import com.ssafy.unsung.dto.KeywordRatioDto;
import com.ssafy.unsung.model.KeywordRatio;
import com.ssafy.unsung.repository.KeywordPercent;
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
    public List<KeywordPercent> findKeyword(String keyword){
        List<KeywordPercent> newsInfo = keywordRatioRepository.findByKeyword(keyword);
        return newsInfo;
    }

    public List<WordCount> wordCount() {return keywordRatioRepository.wordCount();}
}
