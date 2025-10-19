package com.huliua.langChain4j.tools;

import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyRedisTool {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Tool("往redis中插入数据")
    public void setValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Tool("从redis中删除指定的key")
    public void delValue(String key) {
        stringRedisTemplate.delete(key);
    }
}
