package liuyang.testdataredis.template;

import liuyang.testdataredis.entity.Actor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

/**
 * @author liuyang
 * @scine 2021/4/14
 */
@SpringBootTest
@Slf4j
public class RedisTemplateTest {
    // 注意：
    // 现象说明：只要配置了RedisConfig，容器中就没有RedisTemplate<Object, Object>了。
    // Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'org.springframework.data.redis.core.RedisTemplate<java.lang.Object, java.lang.Object>'
    // @Autowired
    // private RedisTemplate<Object, Object> redisTemplate;

    // 订制的
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testValue() {
        // log.info("hello, Redis");

        Actor actor = new Actor();
        actor.setActorId(4);
        actor.setFirstName("yang");
        actor.setLastName("liu");
        actor.setLastUpdate(LocalDateTime.now());

        redisTemplate.delete("liuyang_testjson");
        redisTemplate.opsForValue().set("liuyang_testjson", actor);
        Actor actorFromRedis = (Actor)redisTemplate.opsForValue().get("liuyang_testjson");
        log.info(actorFromRedis.toString());// 在反序列化的时候会出问题
    }
}
