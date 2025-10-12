import axios from 'axios'

// 使用Vite代理路径
const baseURL = '/api'

// 创建axios实例
const api = axios.create({
    baseURL,
    timeout: 60000, // 设置较长的超时时间，因为流式响应可能需要更长时间
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: false // 禁用跨域请求时发送凭证
})

// 请求拦截器
api.interceptors.request.use(
    config => {
        // 可以在这里添加认证信息等
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器
api.interceptors.response.use(
    response => {
        return response
    },
    error => {
        return Promise.reject(error)
    }
)

// 聊天API - 使用axios处理流式响应，使用form格式发送数据
export const streamChat = async (memoryId, message, onChunk) => {
    try {
        // 创建FormData对象
        const formData = new URLSearchParams();
        formData.append('memoryId', memoryId);
        formData.append('message', message);

        // 使用axios发起请求，通过responseType设置为'text'来处理流式响应
        const response = await api.post('/streamChat', formData, {
            responseType: 'text',
            // 启用分块传输编码
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Accept': 'text/event-stream',
            },
            withCredentials: false, // 禁用跨域请求时发送凭证
            // 设置onDownloadProgress回调来处理流式数据
            onDownloadProgress: (progressEvent) => {
                const xhr = progressEvent.event.currentTarget;
                const responseText = xhr.responseText || '';

                // 获取新增的响应文本
                const newChunk = responseText.substring(xhr.lastProcessedLength || 0);
                xhr.lastProcessedLength = responseText.length;

                // 如果有新的数据块且回调函数存在，则调用回调函数
                if (newChunk && onChunk && typeof onChunk === 'function') {
                    onChunk(newChunk);
                }
            }
        });

        // 检查响应状态
        if (response.status !== 200) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return true;
    } catch (error) {
        console.error('Stream chat error:', error);
        throw error;
    }
}

export default api