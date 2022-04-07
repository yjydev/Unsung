package com.ssafy.unsung.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor // 파라미터가 없는 기본 생성자 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자
@Entity
@Table(name = "Keyword_History")
public class Keyword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = " keyword", nullable = false)
	private String keyword;

	@Column(name = "date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDate date;
}
