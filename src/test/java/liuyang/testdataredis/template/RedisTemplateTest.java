package liuyang.testdataredis.template;

import liuyang.testdataredis.entity.Actor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

/**
 * 主要演示定制了
 * 1. 序列化/反序列化器的RedistTempalte
 * 2. RedisAutoConfiguration中注册的RedistTemplate在使用效果上的区别。
 *
 * @author liuyang
 * @scine 2021/4/14
 */
@SpringBootTest
@Slf4j
public class RedisTemplateTest {

    @Autowired
    @Qualifier("redisTemplate3")// 不加貌似也可以 还是加上吧
    private RedisTemplate<String, Object> redisTemplate3;

    // 订制的
    // 当然定制的也可以叫redisTemplate，但如果这样的话，自动配置的类就无法注入了。详细参见RedisAutoConfiguration
    @Autowired
    @Qualifier("redisTemplate2")// 不加貌似也可以 还是加上吧
    private RedisTemplate<String, Object> redisTemplate2;

    // RedisAutoConfiguration中默认定义的RedisTemplate<Object, Object>
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;// 如果不定制，不会影响Java客户端获取的值，但直接通过redis-cli或者redisinsight等工具查看该键值得时候就会出现乱码


    @Test
    void test202402240131VaueCustomized() {
        // log.info("hello, Redis");

        Actor actor = new Actor();
        actor.setActorId(4);
        actor.setFirstName("yang");
        actor.setLastName("liu");
        actor.setLastUpdate(LocalDateTime.now());

        //redisTemplate2.delete("liuyang_testjson");
        //redisTemplate2.opsForValue().set("liuyang_testjson", actor);
        //Actor actorFromRedis = (Actor)redisTemplate2.opsForValue().get("liuyang_testjson");

        redisTemplate3.delete("liuyang_testjson");
        redisTemplate3.opsForValue().set("liuyang_testjson", actor);
        Actor actorFromRedis = (Actor)redisTemplate3.opsForValue().get("liuyang_testjson");

        log.info(actorFromRedis.toString());// 在反序列化的时候会出问题
    }

    @DisplayName("测试一下定制的序列化器以及容器中注册效果")
    @Test
    void test202402240126Customized() {
        log.info("resitTemplate3 = {}, valueSerizliser = {}", redisTemplate3, redisTemplate3.getValueSerializer().getClass().getName());
        log.info("resitTemplate2 = {}, valueSerizliser = {}", redisTemplate2, redisTemplate2.getValueSerializer().getClass().getName());
        log.info("resitTemplate = {}, valueSerizliser = {}", redisTemplate, redisTemplate.getValueSerializer().getClass().getName());
    }


    // 注意：使用定制过序列化规则的redisTemplate2
    // 这里指定了键liuyang_testjson，并且指定使用定制过的序列化器的模板。
    // 所以，使用redisinsight等客户端中看到的键也会是liuyang_testjson
    @Test
    public void testValueCustomized() {
        // log.info("hello, Redis");

        Actor actor = new Actor();
        actor.setActorId(4);
        actor.setFirstName("yang");
        actor.setLastName("liu");
        actor.setLastUpdate(LocalDateTime.now());

        redisTemplate2.delete("liuyang_testjson");
        redisTemplate2.opsForValue().set("liuyang_testjson", actor);
        Actor actorFromRedis = (Actor)redisTemplate2.opsForValue().get("liuyang_testjson");
        log.info(actorFromRedis.toString());// 在反序列化的时候会出问题
    }

    @Test
    public void testValueCustomizedDel() {
        redisTemplate2.delete("liuyang_testjson");
    }

    // 注意：使用在RedisAutoConfiguration中默认的redisTempalte
    // 虽然指定了键liuyang_testjson，但从redisinsight观察该键是否真的是liuyang_testjson
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

    @Test
    public void testValueDel() {
        redisTemplate.delete("liuyang_testjson");
    }
}
