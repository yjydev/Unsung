package com.ssafy.unsung.repository;

import com.ssafy.unsung.model.KeywordRatio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeywordRatioRepository extends JpaRepository<KeywordRatio, Long> {

    @Query(value = "select" +
            "(select count from keywordratio where press = 'a' and keyword = :keyword and label = '0') as negative," +
            "(select count from keywordratio where press = 'a' and keyword = :keyword and label = '1') as positive," +
            "(select count from keywordratio where press = 'a' and keyword = :keyword and label = '2') as neutral," +
            "(select count from keywordratio where press = 'a' and keyword = :keyword and label = '3') as unclassified;", nativeQuery = true)
    List<KeywordPercent> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select keyword, count(keyword) as count from keywordratio where date = curdate() - interval 1 day group by keyword order by count(keyword) desc; ", nativeQuery = true)
    List<WordCount> wordCount();
}
