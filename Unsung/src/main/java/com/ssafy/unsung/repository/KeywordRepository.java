package com.ssafy.unsung.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.unsung.model.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long>{

	List<Keyword> findByKeyword(String keyword);
}
