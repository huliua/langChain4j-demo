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
        <div class="message-text">{{ content }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed} from 'vue';
import {Service, UserFilled} from '@element-plus/icons-vue';


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
  line-height: 1.8;
  font-size: 15px;
  text-align: left;
}
</style>