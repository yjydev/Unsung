package com.web.voice.domain.Topkeyword.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
//@RedisHash("fruit")  //Hash Collection 명시 -> Jpa의 Entity에 해당하는 애노테이션이라고 인지
@AllArgsConstructor
//@Builder
@NoArgsConstructor
public class KeyWord {
    @Id
    private String keyword;

}
