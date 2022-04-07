package com.ssafy.unsung.service;

import com.ssafy.unsung.dto.KeywordRatioDto;
import com.ssafy.unsung.model.KeywordRatio;
import com.ssafy.unsung.repository.*;
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

    @Transactional
    public List<KeywordGraph1> graph1(String keyword, String press){
        List<KeywordGraph1> graphInfo = keywordRatioRepository.graph1(keyword, press);
        return graphInfo;
    }

    @Transactional
    public List<KeywordGraph2> graph2(String keyword){
        List<KeywordGraph2> graphInfo = keywordRatioRepository.graph2(keyword);
        return graphInfo;
    }

    public List<WordCount> wordCount() {return keywordRatioRepository.wordCount();}
}
