package com.huliua.langChain4j.config;

import com.huliua.langChain4j.aiservice.AiChatService;
import com.huliua.langChain4j.tools.MyRedisTool;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiChatConfig {

    @Resource
    private StreamingChatModel openAiStreamingChatModel;

    @Resource
    private ChatMemoryProvider chatMemoryProvider;

    @Resource
    private ContentRetriever contentRetriever;

    @Resource
    private McpToolProvider mcpToolProvider;

    @Resource
    private MyRedisTool myRedisTool;

    /**
     * 创建一个AiChatService
     * 简单方式使用@AiService注解
     */
    @Bean
    public AiChatService aiChatService() {
        return AiServices.builder(AiChatService.class)
                .streamingChatModel(openAiStreamingChatModel) // 流式模型
                .chatMemoryProvider(chatMemoryProvider) // 会保存聊天记录
                .contentRetriever(contentRetriever) // 内容检索器
                .toolProvider(mcpToolProvider)
                .tools(myRedisTool)
                .build();
    }
}
