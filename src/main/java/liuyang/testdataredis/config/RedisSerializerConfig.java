package liuyang.testdataredis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import liuyang.testdataredis.serializer.LocalDateTimeDeserializer;
import liuyang.testdataredis.serializer.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.*;

import java.time.LocalDateTime;

/**
 * 在这里配置不同的序列化器
 *
 * @author xconf
 * @since 2022/11/26
 * @update 2024/2/24 增加序列化器 参考：https://www.bilibili.com/video/BV1Es4y1q7Bf?p=70&vd_source=8bd7b24b38e3e12c558d839b352b32f4
 */
@Configuration
public class RedisSerializerConfig {

    @Bean
    public RedisSerializer<String> stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(initObjectMapper());
        return jackson2JsonRedisSerializer;
    }

    /**
     * 202402240116 add liuyang
     * @return
     */
    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(initObjectMapper());
    }

    /**
     * 定制序列化行为
     * Jackson教程
     * https://www.yiibai.com/jackson/
     *
     * @return
     */
    private ObjectMapper initObjectMapper() {
        // 序列化和反序列化中的细节问题
        // 解决查询缓存转换异常问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // LocalDateTime JSR310
        om.registerModule(new JavaTimeModule()
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer()));
        return om;
    }

    // TODO
    // 问：是否可以换成FastJsonRedisSerializer?
    // 答：https://www.jianshu.com/p/1b3f33a045bf

}
