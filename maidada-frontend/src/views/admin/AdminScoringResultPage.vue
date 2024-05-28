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
        <a-button status="normal" @click="getScoringResultDetail(record.id)"
          >修改
        </a-button>
        <a-popconfirm content="你确定要删除吗?" @ok="doDelete(record)">
          <a-button status="danger">删除</a-button>
        </a-popconfirm>
      </a-space>
    </template>
  </a-table>

  <a-modal
    v-model:visible="visible"
    title="修改评分结果"
    @ok="handleOk"
    @cancel="handleCancel"
    :mask-closable="false"
  >
    <a-form
      ref="formRef"
      :model="form"
      :rules="rules"
      :style="{ width: '480px' }"
      label-align="left"
      auto-label-width
    >
      <a-form-item field="resultName" label="结果名称">
        <a-input v-model="form.resultName" placeholder="请输入结果名称" />
      </a-form-item>
      <a-form-item field="resultDesc" label="结果描述">
        <a-input v-model="form.resultDesc" placeholder="请输入结果描述" />
      </a-form-item>
      <a-form-item field="resultPicture" label="结果图标">
        <a-image
          v-if="form.resultPicture"
          width="120"
          height="100"
          style="margin-right: 10px"
          :src="form.resultPicture"
        />
        <PictureUploader
          :value="form.resultPicture"
          :onChange="(value) => (form.resultPicture = value)"
          :biz="FILE_UPLOAD_BIZ_ENUM['2']"
        />
      </a-form-item>
      <a-form-item field="resultProp" label="结果集">
        <a-input-tag
          v-model="form.resultProp"
          :style="{ width: '320px' }"
          placeholder="请输出结果集，按回车确认"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="resultScoreRange" label="结果得分范围">
        <a-input-number
          v-model="form.resultScoreRange"
          placeholder="请输入结果得分范围"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import {
  deleteScoringResultUsingPost,
  getScoringResultVoByIdUsingGet,
  listScoringResultVoByPageUsingPost,
  updateScoringResultUsingPost,
} from "@/api/scoringResultController";
import message from "@arco-design/web-vue/es/message";
import { dayjs } from "@arco-design/web-vue/es/_utils/date";
import {
  APP_SCORING_STRATEGY_MAP,
  APP_TYPE_MAP,
  FILE_UPLOAD_BIZ_ENUM,
} from "@/constant/app";
import PictureUploader from "@/components/PictureUploader.vue";

const formSearchParams = ref<API.ScoringResultQueryRequest>({});

// 初始化搜索条件（不应该被修改）
const initSearchParams = {
  current: 1,
  pageSize: 10,
};

const searchParams = ref<API.ScoringResultQueryRequest>({
  ...initSearchParams,
});
const dataList = ref<API.ScoringResultVO[]>([]);
const total = ref<number>(0);

/**
 * 加载数据
 */
const loadData = async () => {
  const res = await listScoringResultVoByPageUsingPost(searchParams.value);
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
const doDelete = async (record: API.ScoringResult) => {
  if (!record.id) {
    return;
  }

  const res = await deleteScoringResultUsingPost({
    id: record.id,
  });
  if (res.data.code === 0) {
    loadData();
  } else {
    message.error("删除失败，" + res.data.message);
  }
};

const getScoringResultDetail = async (id: number) => {
  const res = await getScoringResultVoByIdUsingGet({
    id,
  });
  if (res.data.code === 0) {
    form.value = res.data.data;
  } else {
    message.error("获取失败" + res.data.message);
  }
  visible.value = true;
};

const formRef = ref();
const form = ref({
  id: undefined,
  appId: undefined,
  resultDesc: "",
  resultName: "",
  resultPicture: "",
  resultProp: [],
  resultScoreRange: undefined,
} as API.ScoringResultAddRequest);
const rules = {
  id: [{ required: true, message: "请输入ID" }],
  resultName: [
    { required: true, message: "请输入结果名称" },
    { max: 80, message: "最大长度为128" },
  ],
  resultDesc: [{ required: true, message: "请输入结果描述" }],
  appType: [{ required: true, message: "请选择应用类型" }],
  scoringStrategy: [{ required: true, message: "请选择评分策略" }],
};
const visible = ref(false);
const handleOk = async () => {
  const res = await updateScoringResultUsingPost(form.value);
  if (res.data.code === 0) {
    loadData();
    message.success("修改成功");
  } else {
    message.error("修改失败");
  }
  visible.value = false;
};
const handleCancel = () => {
  form.value = {};
  formRef.value.clearValidate();
  visible.value = false;
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
    width: 200,
    fixed: "left",
    align: "center",
  },
  {
    title: "名称",
    dataIndex: "resultName",
    width: 150,
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
    title: "结果属性",
    dataIndex: "resultProp",
    width: 200,
    align: "center",
  },
  {
    title: "评分范围",
    dataIndex: "resultScoreRange",
    width: 90,
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
    ellipsis: true,
    tooltip: true,
    width: 200,
    align: "center",
  },
  {
    title: "应用类型",
    dataIndex: "appType",
    slotName: "appType",
    width: 100,
    align: "center",
  },
  {
    title: "评分策略",
    dataIndex: "scoringStrategy",
    slotName: "scoringStrategy",
    width: 100,
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
