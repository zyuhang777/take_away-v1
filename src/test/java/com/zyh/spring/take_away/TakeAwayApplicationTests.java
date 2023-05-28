package com.zyh.spring.take_away;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class TakeAwayApplicationTests {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Test

    void contextLoads() {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        redisTemplate.delete(arrayList);
    }

}
