package com.ssafy.unsung.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeywordRatioDto {
    private String keyword;
    private String press;
    private Date date;
    private String label;
    private int count;
}
