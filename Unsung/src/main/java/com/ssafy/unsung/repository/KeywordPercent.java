package com.ssafy.unsung.repository;

public interface KeywordPercent {
    Integer getNegative();
    Integer getPositive();
    Integer getNeutral();
    Integer getUnclassified();
    Integer getTotal();
    String getKeyword();
    String getPress();
    String getPeriod();
}
