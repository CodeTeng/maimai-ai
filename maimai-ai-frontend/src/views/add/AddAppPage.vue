<template>
  <div id="addAppPage">
    <a-button type="primary" @click="router.back()">返回应用</a-button>
    <h2 style="margin-bottom: 32px">创建应用</h2>
    <a-form
      :model="form"
      :rules="rules"
      ref="formRef"
      :style="{ width: '480px' }"
      label-align="left"
      auto-label-width
      @submit="handleSubmit"
    >
      <a-form-item field="appName" label="应用名称">
        <a-input v-model="form.appName" placeholder="请输入应用名称" />
      </a-form-item>
      <a-form-item field="appDesc" label="应用描述">
        <a-input v-model="form.appDesc" placeholder="请输入应用描述" />
      </a-form-item>
      <a-form-item field="appIcon" label="应用图标">
        <a-image
          v-if="form.appIcon"
          width="120"
          height="100"
          style="margin-right: 10px"
          :src="form.appIcon"
        />
        <PictureUploader
          :value="form.appIcon"
          :onChange="(value) => (form.appIcon = value)"
          :biz="FILE_UPLOAD_BIZ_ENUM['1']"
        />
      </a-form-item>
      <a-form-item field="appType" label="应用类型">
        <a-select
          v-model="form.appType"
          :style="{ width: '320px' }"
          placeholder="请选择应用类型"
        >
          <a-option
            v-for="(value, key) of APP_TYPE_MAP"
            :value="Number(key)"
            :label="value"
          />
        </a-select>
      </a-form-item>
      <a-form-item field="scoringStrategy" label="评分策略">
        <a-select
          v-model="form.scoringStrategy"
          :style="{ width: '320px' }"
          placeholder="请选择评分策略"
        >
          <a-option
            v-for="(value, key) of APP_SCORING_STRATEGY_MAP"
            :value="Number(key)"
            :label="value"
          />
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 120px">
          提交
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { defineProps, ref, watchEffect, withDefaults } from "vue";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import {
  addAppUsingPost,
  editAppUsingPost,
  getAppVoByIdUsingGet,
} from "@/api/appController";
import {
  APP_SCORING_STRATEGY_MAP,
  APP_TYPE_MAP,
  FILE_UPLOAD_BIZ_ENUM,
} from "@/constant/app";
import PictureUploader from "@/components/PictureUploader.vue";

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => {
    return "";
  },
});

const router = useRouter();

const form = ref({
  appDesc: "",
  appIcon: "",
  appName: "",
  appType: undefined,
  scoringStrategy: undefined,
} as API.AppAddRequest);
const rules = {
  appName: [
    { required: true, message: "请输入应用名称" },
    { max: 80, message: "最大长度为80" },
  ],
  appDesc: [{ required: true, message: "请输入应用描述" }],
  appType: [{ required: true, message: "请选择应用类型" }],
  scoringStrategy: [{ required: true, message: "请选择评分策略" }],
};
const oldApp = ref<API.AppVO>();

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getAppVoByIdUsingGet({
    id: props.id as any,
  });
  if (res.data.code === 0 && res.data.data) {
    oldApp.value = res.data.data;
    form.value = res.data.data;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

// 获取旧数据
watchEffect(() => {
  loadData();
});

/**
 * 提交
 */
const handleSubmit = async () => {
  let res: any;
  // 如果是修改
  if (props.id) {
    res = await editAppUsingPost({
      id: props.id as any,
      ...form.value,
    });
  } else {
    // 创建
    res = await addAppUsingPost(form.value);
  }
  if (res.data.code === 0) {
    message.success("操作成功，即将跳转到应用详情页");
    setTimeout(() => {
      router.push(`/app/detail/${props.id || res.data.data}`);
    }, 3000);
  } else {
    message.error("操作失败，" + res.data.message);
  }
};
</script>
