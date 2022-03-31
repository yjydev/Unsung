package com.ssafy.unsung.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.unsung.model.News;

public interface NewsRepository extends JpaRepository<News, Long>{

	List<News> findByKeyword(String keyword);
}
