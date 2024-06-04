<script setup lang="ts">
import QRCode from "qrcode";
import { ref, defineProps, withDefaults, defineExpose } from "vue";

/**
 * 定义组件属性类型
 */
interface Props {
  title: string;
  link: string;
}

const props = withDefaults(defineProps<Props>(), {
  title: () => "分享",
  link: () => "https://www.baidu.com",
});

const codeImg = ref();
const visible = ref(false);
const openModal = () => {
  visible.value = true;
};
const handleCancel = () => {
  visible.value = false;
};

QRCode.toDataURL(props.link)
  .then((url) => {
    codeImg.value = url;
  })
  .catch((err) => {
    console.error(err);
  });

defineExpose({
  openModal,
});
</script>

<template>
  <a-modal v-model:visible="visible" :footer="false" @cancel="handleCancel">
    <template #title>
      {{ props.title }}
    </template>
    <h4 style="margin-top: 0">复制分享链接</h4>
    <a-typography-paragraph copyable>{{ props.link }}</a-typography-paragraph>
    <h4>手机扫码查看</h4>
    <img :src="codeImg" />
  </a-modal>
</template>

<style scoped></style>
