package liuyang.testdataredis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

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

    @Test
    void test2() {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        log.info("msg = " + operations.get("msg"));
    }

    // 应用举例 雷丰阳 Spring Boot 2 70. 数据访问-redis操作与统计小实验 15:28左右
    @Test
    void statUrl() {
        // 写在Interceptor中。
        String uri = "";// request.getRequestURI();
        stringRedisTemplate.opsForValue().increment(uri);// 这样可以实现URI访问次数统计

        // 补充：
        /*
        registry.addInterceptor(redisUrlCountInterceptor())
                .addPathPatterns("/*")
                .excludePathPatterns("/", "/login", "/css/**", "/fonts/**", "/images/**", "/js/**", "/aa/**", "/sql/**", "/city");
         */
    }
}
