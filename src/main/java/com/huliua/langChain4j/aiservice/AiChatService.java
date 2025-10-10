package com.huliua.langChain4j.aiservice;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel", // 配置阻塞聊天模型
        streamingChatModel = "openAiStreamingChatModel", // 配置流式聊天模型
        chatMemoryProvider = "chatMemoryProvider" // 会话记忆提供器
)
public interface AiChatService {
    String chat(String question);

    @SystemMessage(fromResource = "system.txt")
    Flux<String> chatStream(@MemoryId String memoryId, @UserMessage String question);
}
