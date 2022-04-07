package com.ssafy.unsung.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor // 파라미터가 없는 기본 생성자 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자
@Entity
@Data
@IdClass(KeywordRatio.class)
@Table(name = "Keywordratio")
public class KeywordRatio implements Serializable {

    @Id
    @Column(name = "keyword", nullable = false)
    private String keyword;

    @Id
    @Column(name = "press", nullable = false)
    private String press;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date;

    @Id
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "count", columnDefinition = "int default '0'", nullable = false)
    private int count;
}
