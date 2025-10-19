package com.huliua.langChain4j.aiservice;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

/*@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, // 手动指定模型
        // chatModel = "openAiChatModel", // 配置阻塞聊天模型
        streamingChatModel = "openAiStreamingChatModel", // 配置流式聊天模型
        chatMemoryProvider = "chatMemoryProvider", // 会话记忆提供器
        contentRetriever = "contentRetriever" // 内容检索器
)*/
public interface AiChatService {
    String chat(String question);

    @SystemMessage(fromResource = "system.txt")
    Flux<String> chatStream(@MemoryId String memoryId, @UserMessage String question);
}
