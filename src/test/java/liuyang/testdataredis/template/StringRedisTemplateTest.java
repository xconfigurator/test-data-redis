package liuyang.testdataredis.template;

import io.netty.channel.socket.ChannelInputShutdownEvent;
import liuyang.testdataredis.entity.Actor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
        HashOperations<String, String, String> stringObjectObjectHashOperations2 = stringRedisTemplate.opsForHash();// 20221126 实测 老老实实用这个吧。使用IntelliJ自动生成的这个方泛型有误导嫌疑。到底是哪里的问题？不深究，先记住即可。

        // list
        ListOperations<String, String> stringStringListOperations = stringRedisTemplate.opsForList();

        // set
        SetOperations<String, String> stringStringSetOperations = stringRedisTemplate.opsForSet();

        // zset
        ZSetOperations<String, String> stringStringZSetOperations = stringRedisTemplate.opsForZSet();
    }

    /**
     * 用一个实验看出stringRedisTemplate到底是咋工作的
     * 盲猜是根据Object的toString方法
     * java.lang.ClassCastException: class liuyang.testdataredis.entity.Actor cannot be cast to class java.lang.String (liuyang.testdataredis.entity.Actor is in unnamed module of loader 'app'; java.lang.String is in module java.base of loader 'bootstrap')
     * 猜测错误，如果是使用StringRedisTemplate那么所有操作的键值都需要是String类型，不管是否实现了toString
     *
     * 结论：StringRedisTemplate的键、值，都必须是String类型！
     */
    @Test
    void testStringRedisTemplateFailure() {
        /*
        // Actor重写了toString
        Actor actor = new Actor();
        actor.setActorId(4);
        actor.setFirstName("yang");
        actor.setLastName("liu");
        actor.setLastUpdate(LocalDateTime.now());

        // Foo没有重写toString
        class Foo {
        }
        Foo foo = new Foo();

        final HashOperations<String, Object, Object> stringObjectObjectHashOperations = stringRedisTemplate.opsForHash();
        stringObjectObjectHashOperations.put("testStringRedisTemplateHash", "actor", actor);
        stringObjectObjectHashOperations.put("testStringRedisTemplateHash", "foo", foo);

        final Map<Object, Object> testStringRedisTemplateHash = stringObjectObjectHashOperations.entries("testStringRedisTemplateHash");
        testStringRedisTemplateHash.entrySet().forEach(e -> log.info("{} -> {}", e.getKey(), e.getValue()));
        */
    }

    // 1. string
    // stringRedisTemplate.opsForValue()
    // https://www.runoob.com/redis/redis-keys.html
    // https://www.runoob.com/redis/redis-strings.html
    @Test
    void testValue() {
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
        // 注意！！ 自动生成的泛型有误导！！
        //final HashOperations<String, Object, Object> stringObjectObjectHashOperations = stringRedisTemplate.opsForHash();// 注意，不要使用这个用var自动生成的签名！
        // 注意！！ 调用这个
        final HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();

        Map<String, String> foo = new HashMap<>();
        foo.put("k1", "v1");
        foo.put("k2", "v2");
        //foo.put("k3", "v3");

        // 清理
        //stringRedisTemplate.delete("testStringRedisTemplateOpsForHash");
        // 写入Redis
        operations.putAll("testStringRedisTemplateOpsForHash", foo);// 如果Redis存在该键不会报异常，会直用新Hash覆盖旧的Hash。
        // 从Redis读出
        final Map<String, String> testStringRedisTemplateOpsForHash = operations.entries("testStringRedisTemplateOpsForHash");
        testStringRedisTemplateOpsForHash.entrySet().forEach(e -> log.info("{} -> {}", e.getKey(), e.getValue()));
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
