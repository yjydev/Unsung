package com.ssafy.unsung.repository;

import com.ssafy.unsung.model.KeywordRatio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeywordRatioRepository extends JpaRepository<KeywordRatio, Long> {

    @Query(value = "(select * from month where keyword=:keyword and press='JTBC' and period = '2022-03')" +
            "union all" +
            "(select * from month where keyword=:keyword and press='중앙일보' and period = '2022-03')" +
            "union all" +
            "(select * from month where keyword=:keyword and press='SBS' and period = '2022-03')" +
            "union all" +
            "(select * from month where keyword=:keyword and press='KBS' and period = '2022-03')", nativeQuery = true)
    List<KeywordPercent> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select keyword, total, positive, negative, period from month where keyword=:keyword and press=:press", nativeQuery = true)
    List<KeywordGraph1> graph1(@Param("keyword") String keyword, @Param("press") String press);

    @Query(value = "select press, keyword, sum(total) as total from month where keyword=:keyword group by press;", nativeQuery = true)
    List<KeywordGraph2> graph2(@Param("keyword") String keyword);

    @Query(value = "select keyword, count(keyword) as count from keywordratio where date = curdate() - interval 1 month group by keyword order by count(keyword) desc; ", nativeQuery = true)
    List<WordCount> wordCount();
}
