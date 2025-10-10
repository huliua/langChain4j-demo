package com.huliua.langChain4j.config;

import com.huliua.langChain4j.store.RedisChatMemoryStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiChatConfig {

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    /**
     * 创建一个消息窗口内存，设置最大消息数为20
     * 根据memoryId去获取对应的消息窗口内存，获取不到就创建一个
     * 配置了redisChatMemoryStore，消息会持久化到Redis中存储
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .maxMessages(20)
                .id(memoryId)
                .chatMemoryStore(redisChatMemoryStore)
                .build();
    }
}
