package com.ssafy.unsung.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.unsung.model.News;
import org.springframework.data.jpa.repository.Query;

public interface NewsRepository extends JpaRepository<News, Long>{

	List<News> findByKeyword(String keyword);

	@Query(value = "select keyword, total_count as count from news_analyze order by total_count desc ", nativeQuery = true)
	List<WordCount> wordCount();
}
