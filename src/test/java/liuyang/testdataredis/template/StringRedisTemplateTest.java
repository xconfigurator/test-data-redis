package liuyang.testdataredis.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

/**
 *  各种数据结构
 *  stringRedisTemplate.opsForValue();  // strings
 *  stringRedisTemplate.opsForHash();   // hashes
 *  stringRedisTemplate.opsForList();   // lists
 *  stringRedisTemplate.opsForSet();    // set
 *  stringRedisTemplate.opsForZSet();   // sorted sets
 *
 * @author liuyang
 * @scine 2021/4/15
 */
@SpringBootTest
@Slf4j
public class StringRedisTemplateTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testHello() {
        stringRedisTemplate.opsForValue().set("foo", "bar");
        String foo = stringRedisTemplate.opsForValue().get("foo");
        log.info("foo = {}", foo);
        Assertions.assertEquals("bar", foo);
    }

    @Test
    void testOpsForValue() {
        // string
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();

        String foo = operations.get("foo");
        log.info("foo = " + foo);

        // hash
        HashOperations<String, Object, Object> stringObjectObjectHashOperations = stringRedisTemplate.opsForHash();

        // list
        ListOperations<String, String> stringStringListOperations = stringRedisTemplate.opsForList();

        // set
        SetOperations<String, String> stringStringSetOperations = stringRedisTemplate.opsForSet();

        // zset
        ZSetOperations<String, String> stringStringZSetOperations = stringRedisTemplate.opsForZSet();
    }

    // 1. string
    // stringRedisTemplate.opsForValue()
    // https://www.runoob.com/redis/redis-keys.html
    // https://www.runoob.com/redis/redis-strings.html
    @Test
    void testString() {
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

    // 2. hash
    // stringRedisTemplate.opsForHash();
    // https://www.runoob.com/redis/redis-keys.html
    // https://www.runoob.com/redis/redis-hashes.html
    @Test
    void testHash() {
        // TODO

    }

    // 3. list
    // stringRedisTemplate.opsForList();
    // https://www.runoob.com/redis/redis-keys.html
    // https://www.runoob.com/redis/redis-lists.html
    @Test
    void testList() {
        // TODO

    }

    // 4. set
    // stringRedisTemplate.opsForSet();
    // https://www.runoob.com/redis/redis-keys.html
    // https://www.runoob.com/redis/redis-sets.html
    @Test
    void testSet() {
        // TODO

    }

    // 5. zset
    // stringRedisTemplate.opsForZSet();
    // https://www.runoob.com/redis/redis-keys.html
    // https://www.runoob.com/redis/redis-sorted-sets.html
    @Test
    void testZSet() {
        // TODO

    }

}
