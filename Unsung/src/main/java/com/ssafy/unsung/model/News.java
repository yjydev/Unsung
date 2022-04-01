package com.ssafy.unsung.model;

import javax.annotation.Generated;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor // 파라미터가 없는 기본 생성자 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자
@Entity
@Table(name = "News_Analyze")
public class News {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "code", nullable = false)
	private int code;
	
	@Column(name = "keyword", columnDefinition = "varchar(20)", nullable = false)
	private String keyword;

	//@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.DATE)
	@Column(name = "date", nullable = false, updatable = false, insertable = false)
	private Date date;
	
	@Column(name = "total_count", columnDefinition = "int unsigned", nullable = false)
	private int total_count;
	
	@Column(name = "negative_count", columnDefinition = "int unsigned default '0'", nullable = false)
	private int negative_count;
	
	@Column(name = "neutral_count", columnDefinition = "int unsigned default '0'", nullable = false)
	private int neutral_count;
	
	@Column(name = "positive_count", columnDefinition = "int unsigned default '0'", nullable = false)
	private int positive_count;
	
	@Column(name = "unclassified_count", columnDefinition = "int unsigned default '0'", nullable = false)
	private int unclassified_count;
}
