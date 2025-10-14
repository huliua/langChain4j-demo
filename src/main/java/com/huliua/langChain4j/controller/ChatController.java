package com.huliua.langChain4j.controller;

import com.huliua.langChain4j.aiservice.AiChatService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    @Resource
    private AiChatService aiChatService;

    @RequestMapping("/chat")
    public String chat(@RequestParam(name = "message") String message) {
        return aiChatService.chat(message);
    }

    @RequestMapping(value = "/streamChat")
    public Flux<String> streamChat(String memoryId, String message) {
        return aiChatService.chatStream(memoryId, message);
    }
}
