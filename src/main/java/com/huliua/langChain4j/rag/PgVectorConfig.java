package com.huliua.langChain4j.rag;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PgVectorConfig {

    @Resource
    private EmbeddingModel openAiEmbeddingModel;

    /**
     * 创建pgVector向量数据库对象
     */
    @Bean
    public PgVectorEmbeddingStore pgVectorEmbeddingStore() {
        return PgVectorEmbeddingStore.builder()
                .host("localhost")
                .port(5432)
                .database("langchain4j_demo")
                .user("root")
                .password("123456")
                .table("my_embeddings")
                .dimension(openAiEmbeddingModel.dimension())
                .build();
    }
}
