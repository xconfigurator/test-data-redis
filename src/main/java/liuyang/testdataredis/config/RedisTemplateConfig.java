package liuyang.testdataredis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import liuyang.testdataredis.serializer.LocalDateTimeDeserializer;
import liuyang.testdataredis.serializer.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDateTime;

/**
 * 注意：在RedisAutoConfiguration中已经配置了RedisTemplate和StringRedisTemplate。这里配置是为了修改序列化与反序列化器。
 *
 * @author liuyang
 * @scine 2021/4/15
 *
 * 20210415 测试ok
 *
 * 参考:
 * 1. pdt脚手架
 * 2. https://www.cnblogs.com/yzeng/p/11522411.html
 * 3. https://www.bilibili.com/video/BV1fh411Z7Wd?p=34 序列化器
 *
 * RedisSerializer
 *  |- setKeySerializer/setHashKeySerializer = StringRedisSerializer
 *  |- setValueSerializer/setHashValueSerializer = Jackson2JsonRedisSerializer
 *
 * 20221126
 * 于这个After还是Before 感觉要想使这个序列化器生效应该使用。
 * 但实测，After，Before，或者不加这两个注释，照样能够得到配置序列化反序列化器的效果。
 * //@AutoConfigureAfter(RedisAutoConfiguration.class)
 * //@AutoConfigureBefore(RedisAutoConfiguration.class)
 * 追问：如果不配置RedisTemplate的序列化和反序列化器呢？
 * 实测：要想使用
 *
 *
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)// 202211261633 关于这个After还是Before 感觉要想使这个序列化器生效应该使用，但实测，After，Before
//@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisTemplateConfig {

    // 注意：RedisAutoConfiguration会自动注册
    // 1. stringRedisTemplate:RedisTemplate<String, String>
    // 2. redisTemplate:RedisTemplate<Object, Object>
    // 详细参见RedisAutoConfiguration的源码

    /**
     * 20221126 调整后的redisTemplate注册方法
     *
     * @param lettuceConnectionFactory
     * @param stringRedisSerializer         2022/11/26 设为容器组件
     * @param jackson2JsonRedisSerializer   2022/11/26 设为容器组件
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate2(LettuceConnectionFactory lettuceConnectionFactory
        , RedisSerializer<String> stringRedisSerializer
        , Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        // 配置模板 valueOperations
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        // 配置模板 hashOperations <String, Object, Object> (StringRedisTemplate.opsForHash(), RedisTemplate.opsForHash())
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        // Stream的好像不需要设置
        return redisTemplate;
    }


    // 下面的对象可以理解为使用过程中的“快捷方式”
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate2) {
        return redisTemplate2.opsForValue();
    }

    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate2) {
        return redisTemplate2.opsForHash();
    }

    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate2) {
        return redisTemplate2.opsForList();
    }

    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redistTemplate2) {
        return redistTemplate2.opsForSet();
    }

    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate2) {
        return redisTemplate2.opsForZSet();
    }

    // 不常用额
    @Bean
    public HyperLogLogOperations<String, Object> hyperLogLogOperations(RedisTemplate<String, Object> redisTemplate2) {
        return redisTemplate2.opsForHyperLogLog();
    }

    @Bean
    public GeoOperations<String, Object> geoOperations(RedisTemplate<String, Object> redisTemplate2) {
        // Since Redis 3.2
        return redisTemplate2.opsForGeo();
    }

    @Bean
    public ClusterOperations<String, Object> clusterOperations(RedisTemplate<String, Object> redistTemplate2) {
        return redistTemplate2.opsForCluster();
    }

    @Bean
    public StreamOperations<String, String, Object> stringStringObjectStreamOperations(RedisTemplate<String, Object> redisTemplate2) {
        // Since Redis 5.0 用于MQ场景
        return redisTemplate2.opsForStream();
    }


    /**
     * 通过修改RedisTemplate的序列化规则来完成以JSON格式序列化入Redis的功能
     *
     * @param lettuceConnectionFactory
     * @return
     */
    /*
    @Bean
    public RedisTemplate<String, Object> redisTemplate2(LettuceConnectionFactory lettuceConnectionFactory) {// 这里起名redisTemplate2仅为了保留RedisAutoConfiguration中对的默认redisTempate。
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        // ////////////////////////////////////////////////////////////////////////////////
        // key序列化规则
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        // value序列化规则
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

        // 序列化和反序列化中的细节问题
        // 解决查询缓存转换异常问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // LocalDateTime
        om.registerModule(new JavaTimeModule()
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer()));
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // ////////////////////////////////////////////////////////////////////////////////

        // 配置模板 valueOperations
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        // 配置模板 hashOperations <String, Object, Object> (StringRedisTemplate.opsForHash(), RedisTemplate.opsForHash())
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        // Stream的好像不需要设置

        return redisTemplate;
    }
     */
}