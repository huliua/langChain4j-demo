package com.huliua.langChain4j.controller;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private OpenAiChatModel chatModel;

    @RequestMapping("/chat")
    public String chat(@RequestParam(name = "message", required = true) String message) {
        return chatModel.chat(message);
    }
}
