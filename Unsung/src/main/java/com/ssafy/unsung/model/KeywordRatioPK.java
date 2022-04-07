package com.ssafy.unsung.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class KeywordRatioPK implements Serializable {
    private String keyword;
    private String press;
    private Date date;
    private String label;
}
