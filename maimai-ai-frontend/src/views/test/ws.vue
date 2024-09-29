<script setup lang="ts">
import { onBeforeMount, onMounted, ref } from "vue";
import { useLoginUserStore } from "@/store/userStore";

const message = ref("");
const serverMsg = ref("");
const isConnected = ref(false);
const userStore = useLoginUserStore();
let webSocket;
const connectWebSocket = () => {
  webSocket = new WebSocket(
    `ws://127.0.0.1:8101/api/ws/${userStore.loginUser.id}`
  );
  // 连接 WebSocket 服务器的回调函数
  webSocket.onopen = () => {
    isConnected.value = true;
    console.log("WebSocket 成功连接");
    // 向服务器发送消息
  };

  webSocket.onclose = () => {
    isConnected.value = false;
    console.log("关闭连接");
  };

  // 接收服务器返回数据
  webSocket.onmessage = (event) => {
    console.log(event);
    const msg = event.data;
    console.log("接收到服务器的消息：", msg);
    serverMsg.value = msg;
  };

  webSocket.onerror = (error) => {
    console.error("WebSocket error:", error);
  };
};

// 关闭WebSocket
const disconnectWebSocket = () => {
  isConnected.value = false;
  if (webSocket) {
    webSocket.close();
  }
};

const onSend = () => {
  console.log("发送消息");
  webSocket.send(message.value);
};

onMounted(() => {
  connectWebSocket();
});

onBeforeMount(() => {
  disconnectWebSocket();
});
</script>

<template>
  <div>
    <a-space>
      <a-input
        v-model="message"
        :style="{ width: '320px' }"
        placeholder="请输入"
        allow-clear
      />
      <a-button @click="onSend" type="primary" status="success">发送</a-button>
      {{ isConnected ? "成功连接" : "未连接" }}
      收到服务器信息：
      {{ serverMsg }}
    </a-space>
  </div>
</template>

<style scoped></style>
