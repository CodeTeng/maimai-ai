<template>
  <a-form
    :model="formSearchParams"
    :style="{ marginBottom: '20px' }"
    layout="inline"
    @submit="doSearch"
  >
    <a-form-item field="resultName" label="结果名称">
      <a-input
        v-model="formSearchParams.resultName"
        placeholder="请输入结果名称"
        allow-clear
      />
    </a-form-item>
    <a-form-item field="resultDesc" label="结果描述">
      <a-input
        v-model="formSearchParams.resultDesc"
        placeholder="请输入结果描述"
        allow-clear
      />
    </a-form-item>
    <a-form-item field="appId" label="应用 id">
      <a-input
        v-model="formSearchParams.appId"
        placeholder="请输入应用 id"
        allow-clear
      />
    </a-form-item>
    <a-form-item field="userId" label="用户 id">
      <a-input
        v-model="formSearchParams.userId"
        placeholder="请输入用户 id"
        allow-clear
      />
    </a-form-item>
    <a-form-item field="appType" label="应用类型">
      <a-select
        :style="{ width: '160px' }"
        v-model="formSearchParams.appType"
        placeholder="请选择应用类型"
        allow-clear
      >
        <a-option :value="0">得分类</a-option>
        <a-option :value="1">测评类</a-option>
      </a-select>
    </a-form-item>
    <a-form-item field="scoringStrategy" label="评分策略">
      <a-select
        :style="{ width: '160px' }"
        v-model="formSearchParams.scoringStrategy"
        placeholder="请选择评分策略"
        allow-clear
      >
        <a-option :value="0">自定义</a-option>
        <a-option :value="1">AI</a-option>
      </a-select>
    </a-form-item>
    <a-form-item field="resultScore" label="应用得分">
      <a-input
        v-model="formSearchParams.resultScore"
        placeholder="请输入应用得分"
        allow-clear
      />
    </a-form-item>
    <a-form-item>
      <a-button type="primary" html-type="submit" style="width: 100px">
        搜索
      </a-button>
    </a-form-item>
  </a-form>
  <a-table
    :columns="columns"
    :data="dataList"
    :scroll="scroll"
    :pagination="{
      showTotal: true,
      pageSize: searchParams.pageSize,
      current: searchParams.current,
      total,
    }"
    @page-change="onPageChange"
  >
    <template #resultPicture="{ record }">
      <a-image width="64" :src="record.resultPicture" />
    </template>
    <template #appType="{ record }">
      {{ APP_TYPE_MAP[record.appType] }}
    </template>
    <template #scoringStrategy="{ record }">
      {{ APP_SCORING_STRATEGY_MAP[record.scoringStrategy] }}
    </template>
    <template #createTime="{ record }">
      {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
    </template>
    <template #updateTime="{ record }">
      {{ dayjs(record.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
    </template>
    <template #optional="{ record }">
      <a-space>
        <a-popconfirm content="你确定要删除吗?" @ok="doDelete(record)">
          <a-button status="danger">删除</a-button>
        </a-popconfirm>
      </a-space>
    </template>
  </a-table>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import {
  deleteUserAnswerUsingPost,
  listUserAnswerByPageUsingPost,
  listUserAnswerVoByPageUsingPost,
} from "@/api/userAnswerController";
import API from "@/api";
import message from "@arco-design/web-vue/es/message";
import { dayjs } from "@arco-design/web-vue/es/_utils/date";
import { APP_SCORING_STRATEGY_MAP, APP_TYPE_MAP } from "@/constant/app";

const formSearchParams = ref<API.UserAnswerQueryRequest>({});

// 初始化搜索条件（不应该被修改）
const initSearchParams = {
  current: 1,
  pageSize: 10,
};

const searchParams = ref<API.UserAnswerQueryRequest>({
  ...initSearchParams,
});
const dataList = ref<API.UserAnswerVO[]>([]);
const total = ref<number>(0);

/**
 * 加载数据
 */
const loadData = async () => {
  const res = await listUserAnswerVoByPageUsingPost(searchParams.value);
  // const res = await listUserAnswerByPageUsingPost(searchParams.value);
  if (res.data.code === 0) {
    dataList.value = res.data.data?.records || [];
    total.value = res.data.data?.total || 0;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

/**
 * 执行搜索
 */
const doSearch = () => {
  searchParams.value = {
    ...initSearchParams,
    ...formSearchParams.value,
  };
};

/**
 * 当分页变化时，改变搜索条件，触发数据加载
 * @param page
 */
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

/**
 * 删除
 * @param record
 */
const doDelete = async (record: API.UserAnswer) => {
  if (!record.id) {
    return;
  }

  const res = await deleteUserAnswerUsingPost({
    id: record.id,
  });
  if (res.data.code === 0) {
    loadData();
  } else {
    message.error("删除失败，" + res.data.message);
  }
};

/**
 * 监听 searchParams 变量，改变时触发数据的重新加载
 */
watchEffect(() => {
  loadData();
});

const scroll = {
  x: 2400,
};

// 表格列配置
const columns = [
  {
    title: "id",
    dataIndex: "id",
    ellipsis: true,
    tooltip: true,
    width: 100,
    fixed: "left",
    align: "center",
  },
  {
    title: "选项",
    dataIndex: "choices",
    width: 200,
    align: "center",
  },
  {
    title: "结果 id",
    dataIndex: "resultId",
    ellipsis: true,
    tooltip: true,
    width: 100,
    align: "center",
  },
  {
    title: "名称",
    dataIndex: "resultName",
    width: 200,
    align: "center",
  },
  {
    title: "描述",
    dataIndex: "resultDesc",
    ellipsis: true,
    tooltip: true,
    width: 300,
    align: "center",
  },
  {
    title: "图片",
    dataIndex: "resultPicture",
    slotName: "resultPicture",
    width: 100,
    align: "center",
  },
  {
    title: "得分",
    dataIndex: "resultScore",
    width: 80,
    align: "center",
  },
  {
    title: "应用 id",
    dataIndex: "appId",
    ellipsis: true,
    tooltip: true,
    width: 100,
    align: "center",
  },
  {
    title: "应用名称",
    dataIndex: "appName",
    width: 300,
    align: "center",
  },
  {
    title: "应用类型",
    dataIndex: "appType",
    slotName: "appType",
    width: 90,
    align: "center",
  },
  {
    title: "评分策略",
    dataIndex: "scoringStrategy",
    slotName: "scoringStrategy",
    width: 90,
    align: "center",
  },
  {
    title: "用户 id",
    dataIndex: "userId",
    ellipsis: true,
    tooltip: true,
    width: 100,
    align: "center",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
    width: 200,
    align: "center",
    sortable: {
      sortDirections: ["ascend", "descend"],
    },
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    slotName: "updateTime",
    width: 200,
    align: "center",
    sortable: {
      sortDirections: ["ascend", "descend"],
    },
  },
  {
    title: "操作",
    slotName: "optional",
    align: "center",
  },
];
</script>
