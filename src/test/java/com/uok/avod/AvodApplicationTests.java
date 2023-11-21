package com.uok.avod;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class AvodApplicationTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testStrings() {
        //given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "first";

        //when
        valueOperations.set(key, "helloWorld!");

        //then
        String value = valueOperations.get(key);
        Assertions.assertThat(value).isEqualTo("helloWorld!");
    }

}
