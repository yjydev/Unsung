package com.ssafy.unsung.repository;

import com.ssafy.unsung.model.KeywordRatio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KeywordRatioRepository extends JpaRepository<KeywordRatio, Long> {

    List<KeywordRatio> findByKeyword(String keyword);

    @Query(value = "select keyword, count(keyword) as count from keywordratio where date>=date_add(now(), interval -1 day) group by keyword order by count(keyword) desc; ", nativeQuery = true)
    List<WordCount> wordCount();
}
