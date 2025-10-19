package com.huliua.langChain4j.mcp;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * MCP相关配置类
 */
@Configuration
public class HTCMcpConfig {

    @Bean
    public McpToolProvider mcpToolProvider() {
        // 和MCP通讯
        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("/Users/tigerl/.nvm/versions/node/v23.10.0/bin/npm", "exec", "-y", "howtocook-mcp"))
                .logEvents(true)
                .build();

        // 创建MCP客户端
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .key("MyMCPClient")
                .transport(transport)
                .build();

        // 创建MCP工具提供者
        return McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();
    }
}
