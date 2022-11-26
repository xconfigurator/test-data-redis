package liuyang.testdataredis.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.Arrays;

/**
 * 这里演示使用在RedisTemplateConfig中定制的valueOperations等
 * 在RedisRemplateConfig中定制过的这些Bean都是用的是定制过序列化反序列化器的RedisTemplate
 *
 * @author xconf
 * @since 2022/11/26
 */
@SpringBootTest
@Slf4j
public class RedisTemplateXXOptionsTest {

    @Autowired
    ValueOperations<String, Object> valueOperations;

    @Autowired
    HashOperations<String, String, Object> hashOperations;

    @Autowired
    ListOperations<String, Object> listOperations;

    @Autowired
    SetOperations<String, Object> setOperations;

    @Autowired
    ZSetOperations<String, Object> zSetOperations;

    @Test
    void foo(){
        log.info("{}", valueOperations);
    }

    @Test
    void testValue() {
        valueOperations.set("testValue", "123");
        log.info("{}", valueOperations.get("testValue"));
    }

    @Test
    void testHash() {
        hashOperations.put("testHash1", "k1", 1);
        hashOperations.put("testHash1", "k2", 2);
        hashOperations.put("testHash2", "k1", "v1");
        hashOperations.put("testHash2", "k2", "v2");
        hashOperations.put("testHash2", "k3", "v3");
        hashOperations.put("testHash2", "k4", "v4");
        log.info("{}", hashOperations.get("testHash1", "k2"));
        log.info("{} ", hashOperations.get("testHash2", "k4"));
        log.info("{}", hashOperations.multiGet("testHash2", Arrays.asList("k1", "k2")));
        log.info("{}", hashOperations.entries("testHash2"));// 可以理解为：返回Redis的置顶Map对象
    }

    @Test
    void testList() {
        // TODO

    }

    @Test
    void testSet() {
        // TODO

    }

    @Test
    void testZSet() {
        // TODO

    }
}
