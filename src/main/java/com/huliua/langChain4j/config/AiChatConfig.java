package com.huliua.langChain4j.config;

import com.huliua.langChain4j.store.RedisChatMemoryStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiChatConfig {

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    @Resource
    private PgVectorEmbeddingStore pgVectorEmbeddingStore;

    @Resource
    private EmbeddingModel openAiEmbeddingModel;

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

    /**
     * 内容检索器，用于根据用户输入的查询条件，从知识库中检索出相关的内容
     */
    @Bean
    public ContentRetriever contentRetriever() {
        // =============== 以下内容只要第一次启动的时候放开就行了，会把内容更新到向量数据库中，后续只要从数据库中取数据就行了========
        // 1.加载文档
        List<Document> documents = ClassPathDocumentLoader.loadDocuments("docs");

        // 2.文档切割:每个文档按照段落分隔，每个段落最大的长度为1000字符，每次最多重叠200字符
        DocumentByParagraphSplitter splitter = new DocumentByParagraphSplitter(1000, 200);

        // 3.自定义向量提取器
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                // 设置文档切割器
                .documentSplitter(splitter)
                // 为了提高为文档的质量，为每个切割后的文档碎片 textSegment 添加文档名称作为元信息
                .textSegmentTransformer(textSegment -> TextSegment.from(textSegment.metadata().getString("file_name") + "\n" + textSegment.text(), textSegment.metadata()))
                // 使用的向量模型
                .embeddingModel(openAiEmbeddingModel)
                // 存储向量
                .embeddingStore(pgVectorEmbeddingStore)
                .build();
        ingestor.ingest(documents);
        // ============================================结束========================================================

        // 4.自定义内容检索器
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(pgVectorEmbeddingStore)
                .embeddingModel(openAiEmbeddingModel)
                .maxResults(10) // 返回的最大结果数量
                .minScore(0.7) // 通过余弦相似值筛选出的最小分数
                .build();
    }
}
