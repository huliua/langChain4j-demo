<template>
  <div class="thinking-process">
    <div class="thinking-header">
      <el-icon>
        <Loading />
      </el-icon>
      <span class="thinking-title">AI 思考过程</span>
    </div>
    <div class="thinking-content">
      {{ thinkingContent }}
    </div>
  </div>
</template>

<script setup>
import { Loading } from '@element-plus/icons-vue';
import { computed } from 'vue';

const props = defineProps({
  content: {
    type: String,
    required: true
  }
});

// 提取思考内容
const thinkingContent = computed(() => {
  return props.content;
});
</script>

<style scoped>
.thinking-process {
  background-color: #f0f2f5;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  padding: 12px;
  margin: 8px 0;
  font-size: 14px;
}

.thinking-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  color: #909399;
}

.thinking-title {
  margin-left: 4px;
  font-weight: 500;
}

.thinking-content {
  color: #606266;
  line-height: 1.6;
  white-space: pre-wrap;
  text-align: left;
}
</style>