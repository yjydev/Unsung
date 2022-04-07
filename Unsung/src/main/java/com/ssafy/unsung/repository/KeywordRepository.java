package com.ssafy.unsung.repository;

import java.security.Key;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.unsung.model.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long>{

	@Query(value ="SELECT KEYWORD, COUNT(KEYWORD) as count FROM KEYWORD_HISTORY WHERE DATE >= DATE_ADD(NOW(), INTERVAL -1 HOUR) GROUP BY KEYWORD ORDER BY COUNT(KEYWORD) DESC LIMIT 0, 10;", nativeQuery = true)
	List<TopKeyword> findTopKeyword();
}