<!-- MarkdownRenderer.vue -->
<template>
  <div
      class="markdown-content"
      v-html="renderedContent"
  ></div>
</template>

<script setup>
import {computed, nextTick, watch} from 'vue';
import {marked} from 'marked';
import DOMPurify from 'dompurify';
import Prism from 'prismjs';

// 导入需要的语言支持
import 'prismjs/components/prism-java';
import 'prismjs/components/prism-javascript';
import 'prismjs/components/prism-python';
import 'prismjs/components/prism-typescript';
import 'prismjs/components/prism-css';
import 'prismjs/components/prism-json';
import 'prismjs/components/prism-sql';
import 'prismjs/components/prism-bash';
import 'prismjs/components/prism-markup';
import 'prismjs/components/prism-yaml';
import 'prismjs/components/prism-docker';

// 导入主题样式
import 'prismjs/themes/prism.css';

const props = defineProps({
  content: {
    type: String,
    required: true
  }
});

// 配置 marked
marked.setOptions({
  breaks: true,
  gfm: true,
  highlight: function (code, lang) {
    if (Prism.languages[lang]) {
      try {
        const highlighted = Prism.highlight(code, Prism.languages[actualLang], actualLang);
        // 添加复制按钮
        return `<div class="code-container">
          <button class="copy-button" onclick="copyCode(this)">复制</button>
          <pre class="language-${actualLang}"><code>${highlighted}</code></pre>
        </div>`;
      } catch (e) {
        console.warn('Prism highlight error:', e);
        return code;
      }
    }
    return code;
  }
});

// 渲染 Markdown 内容
const renderedContent = computed(() => {
  const rawMarkup = marked(props.content);
  return DOMPurify.sanitize(rawMarkup);
});

// 监听内容变化，重新高亮代码
watch(() => props.content, async () => {
  await nextTick();
  Prism.highlightAll();
}, {immediate: true});
</script>

<style scoped>
.markdown-content {
  line-height: 1.6;
}

/* 代码块容器样式 */
.markdown-content :deep(pre) {
  position: relative;
  margin: 1em 0;
  padding: 1em;
  border-radius: 6px;
  background-color: #f8f9fa;
  overflow: auto;
}

/* 行内代码样式 */
.markdown-content :deep(code) {
  padding: 2px 4px;
  font-size: 0.9em;
  background-color: rgba(27, 31, 35, 0.05);
  border-radius: 3px;
  font-family: Consolas, Monaco, 'Andale Mono', monospace;
}

/* 代码块内的代码样式 */
.markdown-content :deep(pre code) {
  padding: 0;
  background: none;
  font-size: 1em;
  line-height: 1.5;
}

/* 语言标签样式 */
.markdown-content :deep(pre[class*="language-"])::before {
  content: attr(class);
  position: absolute;
  top: 0;
  right: 0;
  padding: 2px 8px;
  font-size: 0.8em;
  background-color: #f0f0f0;
  border-bottom-left-radius: 4px;
  color: #666;
}

/* 复制按钮样式 */
.markdown-content :deep(.copy-button) {
  position: absolute;
  top: 4px;
  right: 4px;
  padding: 4px 8px;
  font-size: 12px;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 3px;
  cursor: pointer;
  opacity: 0.7;
  transition: opacity 0.2s;
}

.markdown-content :deep(.copy-button:hover) {
  opacity: 1;
}

/* 其他 Markdown 元素的样式 */
.markdown-content :deep(h1),
.markdown-content :deep(h2),
.markdown-content :deep(h3),
.markdown-content :deep(h4),
.markdown-content :deep(h5),
.markdown-content :deep(h6) {
  margin: 1em 0 0.5em 0;
}

.markdown-content :deep(ul),
.markdown-content :deep(ol) {
  margin: 0.5em 0;
  padding-left: 2em;
}

.markdown-content :deep(blockquote) {
  border-left: 4px solid #ddd;
  margin: 0.5em 0;
  padding-left: 1em;
  color: #666;
}

.markdown-content :deep(table) {
  border-collapse: collapse;
  margin: 0.5em 0;
}

.markdown-content :deep(table th),
.markdown-content :deep(table td) {
  border: 1px solid #ddd;
  padding: 6px 12px;
  text-align: left;
}

.markdown-content :deep(table th) {
  background-color: #f5f5f5;
}
</style>
