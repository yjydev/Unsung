package com.web.voice.domain.Topkeyword.repository;

import com.web.voice.domain.Topkeyword.dto.KeyWord;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class KeyWordRepository {

    // DB 조회 횟수
    private static int dbCount = 0;

    public List<KeyWord> createBySize(String size) {
        // DB 조회를 했다고 가정하여 카운트를 올린다.
        dbCount++;
        ArrayList<KeyWord> boards = new ArrayList<KeyWord>();
        int count = Integer.parseInt(size);


        for (int i = 0; i < count; i++) {
            boards.add(new KeyWord(i+" "));
            System.out.println(i + " KEYword! ");
        }

        return boards;
    }

    public static int getDbCount() {
        return dbCount;
    }

}
