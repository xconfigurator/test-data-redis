package liuyang.testdataredis.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author liuyang
 * @scine 2021/9/17
 */
@SpringBootTest
@Slf4j
public class StringRedisTemplateUseCaseTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 应用举例 雷丰阳 Spring Boot 2 70. 数据访问-redis操作与统计小实验 15:28左右
    //@Test
    @RepeatedTest(10)
    void statUrl() {
        // 写在Interceptor中。
        String uri = "/foo/bar";// request.getRequestURI();
        stringRedisTemplate.opsForValue().increment(uri);// 这样可以实现URI访问次数统计

        // 补充：
        /*
        registry.addInterceptor(redisUrlCountInterceptor())
                .addPathPatterns("/*")
                .excludePathPatterns("/", "/login", "/css/**", "/fonts/**", "/images/**", "/js/**", "/aa/**", "/sql/**", "/city");
         */

        String s = stringRedisTemplate.opsForValue().get(uri);
        log.info("uri counter = {}", s);
    }
}
