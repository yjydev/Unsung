package com.ssafy.unsung.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {

	private int id;
	private int code;
	private String keyword;
	private String date;
	private int total_count;
	private int negative_count;
	private int neutral_count;
	private int positive_count;
	private int unclassified_count;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    private String image;
    private String title;
    private String press;
    private String url;
}
