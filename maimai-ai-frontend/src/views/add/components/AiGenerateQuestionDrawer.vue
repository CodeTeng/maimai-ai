<template>
  <a-button type="outline" @click="handleClick" :loading="!submitting"
    >AI 生成题目
  </a-button>
  <a-spin
    tip="生成题目中"
    :style="{ display: submitting ? 'none' : 'block' }"
    style="position: fixed; top: 100px; left: 0; right: 0"
  />
  <a-drawer
    :width="340"
    :visible="visible"
    :mask-closable="false"
    :footer="false"
    @ok="visible = false"
    @cancel="handleCancel"
    unmountOnClose
  >
    <template #title>AI 生成题目</template>
    <div>
      <a-form
        ref="formRef"
        :model="form"
        label-align="left"
        auto-label-width
        @submit="handleSubmit"
      >
        <a-form-item label="应用 id">
          {{ appId }}
        </a-form-item>
        <a-form-item
          field="questionNumber"
          label="题目数量"
          :validate-trigger="['blur']"
          :rules="[{ required: true, message: '题目数量是必填项' }]"
        >
          <a-input-number
            min="1"
            max="10"
            v-model="form.questionNumber"
            placeholder="请输入题目数量"
          />
        </a-form-item>
        <a-form-item
          field="optionNumber"
          label="选项数量"
          :validate-trigger="['blur']"
          :rules="[{ required: true, message: '选项数量是必填项' }]"
        >
          <a-input-number
            min="1"
            max="6"
            v-model="form.optionNumber"
            placeholder="请输入选项数量"
          />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" style="width: 120px">
            一键生成
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { defineProps, reactive, ref, withDefaults } from "vue";
import message from "@arco-design/web-vue/es/message";

interface Props {
  appId: string;
  onSuccess?: (result: API.QuestionContentDTO[]) => void;
}

const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});

const form = reactive({
  optionNumber: 4,
  questionNumber: 10,
} as API.AiGenerateQuestionRequest);

const visible = ref(false);
const submitting = ref(true);
const formRef = ref();

const handleClick = () => {
  visible.value = true;
};
const handleCancel = () => {
  formRef.value?.clearValidate();
  visible.value = false;
};

/**
 * 提交
 */
const handleSubmit = async () => {
  if (!props.appId) {
    return;
  }
  submitting.value = false;
  const eventSource = new EventSource(
    "http://localhost:8101/api/question/ai_generate/sse?appId=" +
      props.appId +
      "&questionNumber=" +
      form.questionNumber +
      "&optionNumber=" +
      form.optionNumber,
    { withCredentials: true }
  );
  // 关闭抽屉
  handleCancel();
  // 接收消息
  eventSource.onmessage = (event) => {
    if (props.onSuccess) {
      if (event.data) {
        props.onSuccess(JSON.parse(event.data));
      }
    } else {
      message.success("生成题目成功");
    }
  };
  // 生成结束，关闭连接
  eventSource.onerror = (event) => {
    if (event.eventPhase === eventSource.CLOSED) {
      submitting.value = true;
      message.success(`生成题目完成，一共生成${form.questionNumber}道题目`);
      eventSource.close();
    } else {
      message.error("生成失败");
      eventSource.close();
    }
  };

  // submitting.value = true;
  // const res = await aiGenerateQuestionUsingPost({
  //   appId: props.appId as any,
  //   ...form,
  // });
  // if (res.data.code === 0 && res.data.data.length > 0) {
  //   if (props.onSuccess) {
  //     props.onSuccess(res.data.data);
  //   } else {
  //     message.success("生成题目成功");
  //   }
  //   // 关闭抽屉
  //   handleCancel();
  // } else {
  //   message.error("操作失败，" + res.data.message);
  // }
  // submitting.value = false;
};
</script>
