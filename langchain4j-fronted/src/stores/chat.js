import {defineStore} from 'pinia'
import {computed, ref} from 'vue'
import {v4 as uuidv4} from 'uuid'
import {streamChat} from '../api'

export const useChatStore = defineStore('chat', () => {
    // 状态
    const messages = ref([])
    const loading = ref(false)
    const currentMemoryId = ref(uuidv4())

    // 计算属性
    const messageList = computed(() => messages.value)

    // 动作
    function addMessage(role, content) {
        messages.value.push({
            id: uuidv4(),
            role,
            content,
            timestamp: new Date().toISOString()
        })
    }

    function clearMessages() {
        messages.value = []
        currentMemoryId.value = uuidv4()
    }

    async function sendMessage(content) {
        if (!content.trim()) return

        // 添加用户消息
        addMessage('user', content)

        // 设置加载状态
        loading.value = true

        try {
            // 初始化AI回复
            addMessage('ai', '')

            // 获取最后一条消息的索引（AI回复）
            const lastIndex = messages.value.length - 1
            let aiResponse = ''

            // 处理每个数据块的回调函数
            const handleChunk = (chunk) => {
                // 解析数据
                const lines = chunk.split('\n');
                for (const line of lines) {
                    if (line.startsWith('data:')) {
                        aiResponse += line.split(':')[1] || '';
                    }
                }
                // 更新最后一条消息（AI回复）
                if (lastIndex >= 0 && messages.value[lastIndex].role === 'ai') {
                    messages.value[lastIndex].content = aiResponse
                }
            }

            // 发送请求并处理流式响应
            await streamChat(currentMemoryId.value, content, handleChunk)
        } catch (error) {
            console.error('发送消息失败:', error)
            // 添加错误消息
            addMessage('system', '发送消息失败，请重试')
        } finally {
            // 重置加载状态
            loading.value = false
        }
    }

    return {
        messages,
        loading,
        currentMemoryId,
        messageList,
        addMessage,
        clearMessages,
        sendMessage
    }
})