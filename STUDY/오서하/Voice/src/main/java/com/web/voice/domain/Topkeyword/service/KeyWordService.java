package com.web.voice.domain.Topkeyword.service;

import com.web.voice.domain.Topkeyword.dto.KeyWord;
import com.web.voice.domain.Topkeyword.repository.KeyWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyWordService {
    @Autowired
    KeyWordRepository keyWordRepository;

    @Cacheable(key = "#size", value = "getKeywords")
    public List<KeyWord> getKeywords(String size) {
        return keyWordRepository.createBySize(size);
    }

    public int getDbCount() {
        return keyWordRepository.getDbCount();
    }
}
