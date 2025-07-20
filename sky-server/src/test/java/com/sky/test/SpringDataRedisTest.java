package com.sky.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import java.util.*;

//@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate(){
        // 看是否创建成功
        System.out.println(redisTemplate);

        ValueOperations valueOperations=redisTemplate.opsForValue();
        HashOperations hashOperations=redisTemplate.opsForHash();
        ListOperations listOperations=redisTemplate.opsForList();
        SetOperations setOperations=redisTemplate.opsForSet();
        ZSetOperations zSetOperations=redisTemplate.opsForZSet();
    }

    @Test
    public void testString(){
        ValueOperations valueOperations=redisTemplate.opsForValue();
        valueOperations.set("city","北京beijing");
        System.out.println((String)valueOperations.get("city")); // 输出 北京beijing
    }

    @Test
    public void testHash() {
        HashOperations hashOperations=redisTemplate.opsForHash();
        hashOperations.put("100","名字","张哥");
        hashOperations.put("100","年龄","19");
        String name=(String)hashOperations.get("100","名字");
        System.out.println(name); //输出张哥

        Set keys=hashOperations.keys("100");
        System.out.println(keys);
        List values=hashOperations.values("100");
        System.out.println(values);
    }
}
