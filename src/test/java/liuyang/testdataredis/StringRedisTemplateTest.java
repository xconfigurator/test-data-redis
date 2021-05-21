package liuyang.testdataredis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author liuyang
 * @scine 2021/4/15
 */
@SpringBootTest
@Slf4j
public class StringRedisTemplateTest {
    /*
        stringRedisTemplate.opsForValue();// strings
        stringRedisTemplate.opsForHash();// hashes
        stringRedisTemplate.opsForList();// lists
        stringRedisTemplate.opsForSet();// set
        stringRedisTemplate.opsForZSet();// sorted sets
    */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testValue() {
        // log.info("StringRedisTemplateTest");

        // delete
        stringRedisTemplate.delete("heihei");// 删除一个不存在的key。 没有异常
        stringRedisTemplate.delete("msg");

        // append
        stringRedisTemplate.opsForValue().append("msg", "hello");
        stringRedisTemplate.opsForValue().append("msg", ", world");
        stringRedisTemplate.opsForValue().append("msg", " via StringRedisTemplate");

        // get
        log.info(stringRedisTemplate.opsForValue().get("msg"));
        Assertions.assertEquals("hello, world via StringRedisTemplate", stringRedisTemplate.opsForValue().get("msg"));
    }


}
