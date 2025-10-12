<template>
  <div class="chat-container">
    <div class="chat-header">
      <h2>AI 聊天助手</h2>
      <el-button type="primary" @click="newChat" plain>
        <el-icon>
          <Plus/>
        </el-icon>
        新建会话
      </el-button>
    </div>

    <div class="chat-messages" ref="messagesContainer">
      <template v-if="messageList.length > 0">
        <chat-message
            v-for="message in messageList"
            :key="message.id"
            :role="message.role"
            :content="message.content"
        />
      </template>
      <div v-else class="empty-chat">
        <el-empty description="开始一个新的对话吧"/>
      </div>
    </div>

    <div class="chat-input">
      <el-input
          v-model="userInput"
          type="textarea"
          :rows="3"
          placeholder="请输入您的问题..."
          resize="none"
          @keydown.enter.prevent="handleEnter"
      />
      <div class="chat-actions">
        <el-button @click="clearInput" plain>
          <el-icon>
            <Delete/>
          </el-icon>
          清空
        </el-button>
        <el-button
            type="primary"
            @click="sendMessage"
            :loading="loading"
            :disabled="!userInput.trim()"
        >
          <el-icon v-if="!loading">
            <Position/>
          </el-icon>
          发送
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, nextTick, ref, watch} from 'vue';
import {useChatStore} from '../stores/chat';
import ChatMessage from './ChatMessage.vue';
import {Delete, Plus, Position} from '@element-plus/icons-vue';

// 使用Pinia存储
const chatStore = useChatStore();
const messageList = computed(() => chatStore.messageList);
const loading = computed(() => chatStore.loading);

// 用户输入
const userInput = ref('');
const messagesContainer = ref(null);

// 发送消息
const sendMessage = async () => {
  if (!userInput.value.trim() || loading.value) return;

  const message = userInput.value;
  userInput.value = '';

  await chatStore.sendMessage(message);
};

// 清空输入
const clearInput = () => {
  userInput.value = '';
};

// 新建会话
const newChat = () => {
  chatStore.clearMessages();
  userInput.value = '';
};

// 处理回车键
const handleEnter = (e) => {
  if (e.ctrlKey || e.metaKey) {
    // Ctrl+Enter 或 Cmd+Enter 发送消息
    sendMessage();
  } else {
    // 普通回车换行
    userInput.value += '\n';
  }
};

// 监听消息列表变化，自动滚动到底部
watch(messageList, () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  });
}, {deep: true});
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 90vh; /* 减少高度为视口高度的90% */
  width: 90vw; /* 使用视口宽度的90% */
  max-width: 1400px; /* 增加最大宽度 */
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid #eaeaea;
  margin-bottom: 16px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.empty-chat {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #909399;
}

.chat-input {
  border-top: 1px solid #eaeaea;
  padding-top: 16px;
}

.chat-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
  gap: 8px;
}
</style>