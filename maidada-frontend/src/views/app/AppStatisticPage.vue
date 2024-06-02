<script setup lang="ts">
import VChart from "vue-echarts";
import "echarts";
import { computed, onMounted, ref } from "vue";
import {
  getAppAnswerCountUsingGet,
  getAppAnswerResultCountUsingGet,
} from "@/api/appStatisticController";
import message from "@arco-design/web-vue/es/message";

const appAnswerCountList = ref<API.AppAnswerCountVO[]>([]);
const appAnswerResultCountList = ref<API.AppAnswerResultCountVO[]>([]);
const selectValue = ref<number>();

/**
 * 加载数据
 */
const loadAppAnswerCountData = async () => {
  const res = await getAppAnswerCountUsingGet({
    value: selectValue.value,
  });
  if (res.data.code === 0) {
    appAnswerCountList.value = res.data.data || [];
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};
const loadAppAnswerResultCountData = async (appId: number) => {
  if (!appId) return;
  const res = await getAppAnswerResultCountUsingGet({
    appId,
  });
  if (res.data.code === 0) {
    appAnswerResultCountList.value = res.data.data || [];
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

const appAnswerCountOptions = computed(() => {
  return {
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    xAxis: {
      type: "category",
      data: appAnswerCountList.value.map((item) => item.appName),
      name: "app 名称",
    },
    yAxis: {
      type: "value",
      name: "应用答题数量",
    },
    series: [
      {
        data: appAnswerCountList.value.map((item) => item.answerCount),
        type: "bar",
      },
    ],
  };
});

const appAnswerResultCountOptions = computed(() => {
  return {
    tooltip: {
      trigger: "item",
    },
    legend: {
      orient: "vertical",
      left: "left",
    },
    series: [
      {
        name: "应用回答数量",
        type: "pie",
        radius: "50%",
        data: appAnswerResultCountList.value.map((item) => {
          return {
            value: item.resultCount,
            name: item.resultName,
          };
        }),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: "rgba(0, 0, 0, 0.5)",
          },
        },
      },
    ],
  };
});

onMounted(() => {
  loadAppAnswerCountData();
});
</script>

<template>
  <div class="appStatisticPage">
    <a-space direction="vertical" fill>
      <a-card :title="`热门应用TOP${selectValue ?? 5}排行榜`" hoverable>
        <template #extra>
          <a-select
            size="small"
            :style="{ width: '320px' }"
            v-model="selectValue"
            placeholder="请选择"
            @change="loadAppAnswerCountData"
          >
            <a-option>3</a-option>
            <a-option>5</a-option>
            <a-option>10</a-option>
          </a-select>
        </template>
        <v-chart
          :option="appAnswerCountOptions"
          style="height: 400px"
        ></v-chart>
      </a-card>
      <a-card title="应用回答分布统计" hoverable>
        <template #extra>
          <a-input-search
            :style="{ width: '320px' }"
            placeholder="请输入 appId"
            button-text="搜索"
            search-button
            @search="(value) => loadAppAnswerResultCountData(value)"
          />
        </template>
        <v-chart
          :option="appAnswerResultCountOptions"
          style="height: 400px"
        ></v-chart>
      </a-card>
    </a-space>
  </div>
</template>

<style scoped></style>
