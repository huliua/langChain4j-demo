<template>
  <div class="message" :class="{ 'message-user': isUser, 'message-ai': !isUser }">
    <div class="message-content">
      <div class="message-avatar">
        <el-avatar :size="40"
                   :style="{ background: 'white', border: isUser ? '1px solid #409EFF' : '1px solid #67C23A' }">
          <template #default>
            <el-icon v-if="isUser" :color="'#409EFF'">
              <UserFilled/>
            </el-icon>
            <el-icon v-else :color="'#67C23A'">
              <Service/>
            </el-icon>
          </template>
        </el-avatar>
      </div>
      <div class="message-bubble">
        <template v-if="isUser">
          <!-- 用户消息直接显示 -->
          <div class="message-text">{{ content }}</div>
        </template>
        <template v-else>
          <!-- AI消息需要分离思考过程和正式回复 -->
          <thinking-process 
            v-if="thinkingContent" 
            :content="thinkingContent" 
          />
          <div class="message-text">
            <MarkdownRenderer :content="normalContent"/>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed} from 'vue';
import {Service, UserFilled} from '@element-plus/icons-vue';
import ThinkingProcess from './ThinkingProcess.vue';
import MarkdownRenderer from './MarkdownRenderer.vue';


const props = defineProps({
  role: {
    type: String,
    required: true,
    validator: (value) => ['user', 'ai', 'system'].includes(value)
  },
  content: {
    type: String,
    required: true
  }
});

const isUser = computed(() => props.role === 'user');

// 提取思考过程内容
const thinkingContent = computed(() => {
  // 如果有结束标签，就展示标签之内的内容
  if (props.content.indexOf('</think>') > -1) {
    const thinkingMatch = props.content.match(/<think>([\s\S]*?)<\/think>/);
    return thinkingMatch ? thinkingMatch[1] : '';
  } else if (props.content.startsWith('<think>')) {
    // 如果没有结束标签，就展示从开始标签到结尾的内容
    const thinkingMatch = props.content.match(/<think>([\s\S]*)/);
    return thinkingMatch ? thinkingMatch[1] : '';
  } else {
    return '';
  }
});

// 提取正常回复内容
const normalContent = computed(() => {
  if (props.content.startsWith('<think>') ) {
    if (props.content.indexOf('</think>') > -1) {
      return props.content.replace(/<think>[\s\S]*?<\/think>/g, '').trim();
    }
    return '思考中......';
  }
  return props.content;
});
</script>

<style scoped>
.message {
  display: flex;
  margin-bottom: 16px;
  width: 100%;
}

.message-user {
  justify-content: flex-end;
}

.message-ai {
  justify-content: flex-start;
}

.message-content {
  display: flex;
  max-width: 90%;
}

.message-user .message-content {
  flex-direction: row-reverse;
}

.message-avatar {
  margin: 0 8px;
  align-self: flex-start;
}

.message-bubble {
  padding: 12px 16px;
  border-radius: 12px;
  word-break: break-word;
  white-space: pre-wrap;
}

.message-user .message-bubble {
  background-color: #409EFF;
  color: white;
  border-top-right-radius: 2px;
}

.message-ai .message-bubble {
  background-color: #f4f6f8;
  color: #333;
  border-top-left-radius: 2px;
}

.message-text {
  white-space: pre-wrap;
  line-height: 1.8;
  font-size: 15px;
  text-align: left;
}


/* 如果需要覆盖 MarkdownRenderer 的某些样式 */
.message-text :deep(.markdown-content) {
  /* 调整内边距以匹配消息框 */
  padding: 0;
  margin: 0;
}

/* 调整代码块样式以适应消息框 */
.message-text :deep(pre) {
  margin: 8px 0;
  background-color: #f8f9fa;
}

/* 调整行内代码样式 */
.message-text :deep(code) {
  background-color: #e9ecef;
  padding: 2px 4px;
  border-radius: 3px;
}
</style>