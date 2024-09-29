<template>
  <a-row id="globalHeader" align="center" :wrap="false">
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :selected-keys="selectedKeys"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div class="titleBar">
            <img class="logo" src="../assets/logo.jpg" />
            <div class="title">慧问快答</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="150px">
      <a-space>
        <a-avatar
          v-if="loginUserStore.loginUser.id"
          :trigger-icon-style="{ color: '#3491FA' }"
          :style="{ backgroundColor: '#168CFF' }"
          :auto-fix-font-size="false"
          @click="logout"
        >
          <img alt="avatar" :src="loginUserStore.loginUser.userAvatar" />
          <template #trigger-icon>
            <IconStop />
          </template>
        </a-avatar>
        <div v-if="loginUserStore.loginUser.id">
          {{ loginUserStore.loginUser.userName ?? "无名" }}
        </div>
        <div v-else>
          <a-button type="primary" href="/user/login">登录</a-button>
        </div>
      </a-space>
    </a-col>
  </a-row>
  <a-modal
    v-model:visible="visible"
    hide-title
    :mask-closable="false"
    width="auto"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <div>你确定要退出吗？</div>
  </a-modal>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useLoginUserStore } from "@/store/userStore";
import checkAccess from "@/access/checkAccess";
import { IconStop } from "@arco-design/web-vue/es/icon";
import { Message } from "@arco-design/web-vue";
import { userLogoutUsingPost } from "@/api/userController";

const loginUserStore = useLoginUserStore();

const router = useRouter();
// 当前选中的菜单项
const selectedKeys = ref(["/"]);
// 路由跳转时，自动更新选中的菜单项
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
});

// 展示在菜单栏的路由数组
const visibleRoutes = computed(() => {
  return routes.filter((item) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    // 根据权限过滤菜单
    if (!checkAccess(loginUserStore.loginUser, item.meta?.access as string)) {
      return false;
    }
    return true;
  });
});

// 点击菜单跳转到对应页面
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

const visible = ref(false);
const handleOk = async () => {
  // 调用退出登录函数
  await userLogoutUsingPost();
  Message.success("退出成功");
  loginUserStore.setLoginUser({});
  visible.value = false;
};
const handleCancel = () => {
  visible.value = false;
};
const logout = () => {
  visible.value = true;
};
</script>

<style scoped>
#globalHeader {
}

.titleBar {
  display: flex;
  align-items: center;
}

.title {
  margin-left: 16px;
  color: black;
}

.logo {
  height: 48px;
}
</style>
