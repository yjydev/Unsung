package com.web.voice.domain.Topkeyword.contoller;

import com.web.voice.domain.Topkeyword.dto.KeyWord;
import com.web.voice.domain.Topkeyword.service.KeyWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KeyWordController {
    @Autowired
    private KeyWordService keyWordService;

    @GetMapping
    public ResponseEntity<?> getKeyWords(String size){
        List<KeyWord> keyWords = keyWordService.getKeywords(size);
        return new ResponseEntity<>(keyWords, HttpStatus.OK);
    }

    @GetMapping("count")
    public int count() {
        return keyWordService.getDbCount();
    }
}
