<template>
  <a-form
    :model="formSearchParams"
    :style="{ marginBottom: '20px' }"
    layout="inline"
    @submit="doSearch"
  >
    <a-form-item field="appName" label="应用名称">
      <a-input
        v-model="formSearchParams.appName"
        placeholder="请输入应用名称"
        allow-clear
      />
    </a-form-item>
    <a-form-item field="appDesc" label="应用描述">
      <a-input
        v-model="formSearchParams.appDesc"
        placeholder="请输入应用描述"
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
    <a-form-item field="reviewStatus" label="审核状态">
      <a-select
        :style="{ width: '160px' }"
        v-model="formSearchParams.reviewStatus"
        placeholder="请选择审核状态"
        allow-clear
      >
        <a-option :value="0">待审核</a-option>
        <a-option :value="1">通过</a-option>
        <a-option :value="2">拒绝</a-option>
      </a-select>
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
    <template #appIcon="{ record }">
      <a-image width="64" :src="record.appIcon" />
    </template>
    <template #appType="{ record }">
      {{ APP_TYPE_MAP[record.appType] }}
    </template>
    <template #scoringStrategy="{ record }">
      {{ APP_SCORING_STRATEGY_MAP[record.scoringStrategy] }}
    </template>
    <template #reviewStatus="{ record }">
      {{ REVIEW_STATUS_MAP[record.reviewStatus] }}
    </template>
    <template #reviewerUserName="{ record }">
      {{ record }}
    </template>
    <template #createUserName="{ record }">
      {{ record }}
    </template>
    <template #reviewTime="{ record }">
      {{
        record.reviewTime &&
        dayjs(record.reviewTime).format("YYYY-MM-DD HH:mm:ss")
      }}
    </template>
    <template #createTime="{ record }">
      {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
    </template>
    <template #updateTime="{ record }">
      {{ dayjs(record.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
    </template>
    <template #optional="{ record }">
      <a-space>
        <a-button
          v-if="record.reviewStatus !== REVIEW_STATUS_ENUM.PASS"
          status="success"
          @click="
            () => {
              rejectForm.id = record.id;
              rejectForm.reviewStatus = REVIEW_STATUS_ENUM.PASS;
              rejectForm.reviewMessage = '';
              doReview();
            }
          "
        >
          通过
        </a-button>
        <a-button
          v-if="record.reviewStatus !== REVIEW_STATUS_ENUM.REJECT"
          status="warning"
          @click="
            () => {
              rejectForm.id = record.id;
              rejectVisible = true;
            }
          "
        >
          拒绝
        </a-button>
        <a-button status="normal" @click="getAppDetail(record.id)"
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
    title="修改应用"
    @ok="handleOk"
    @cancel="handleCancel"
    :mask-closable="false"
  >
    <a-form :model="form" :rules="rules" ref="formRef">
      <a-form-item field="appName" label="应用名称">
        <a-input
          v-model="form.appName"
          :max-length="80"
          allow-clear
          show-word-limit
          placeholder="请输入应用名称"
        />
      </a-form-item>
      <a-form-item field="appDesc" label="应用描述">
        <a-textarea
          v-model="form.appDesc"
          placeholder="请输入应用描述"
          allow-clear
        />
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
          allow-clear
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
          allow-clear
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
    </a-form>
  </a-modal>

  <a-modal
    v-model:visible="rejectVisible"
    title="审核不通过"
    @ok="doReview"
    @cancel="handleRejectCancel"
    :mask-closable="false"
  >
    <a-form :model="rejectForm" ref="rejectFormRef">
      <a-form-item
        field="reviewMessage"
        label="审核信息"
        :validate-trigger="['blur']"
        :rules="[{ required: true, message: '审核信息是必填项' }]"
      >
        <a-textarea
          allow-clear
          v-model="rejectForm.reviewMessage"
          placeholder="请输入审核信息"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import {
  deleteAppUsingPost,
  doAppReviewUsingPost,
  getAppVoByIdUsingGet,
  listAppByPageUsingPost,
  updateAppUsingPost,
} from "@/api/appController";
import message from "@arco-design/web-vue/es/message";
import { dayjs } from "@arco-design/web-vue/es/_utils/date";
import {
  APP_SCORING_STRATEGY_MAP,
  APP_TYPE_MAP,
  FILE_UPLOAD_BIZ_ENUM,
  REVIEW_STATUS_ENUM,
  REVIEW_STATUS_MAP,
} from "@/constant/app";
import PictureUploader from "@/components/PictureUploader.vue";

const formSearchParams = ref<API.AppQueryRequest>({});

// 初始化搜索条件（不应该被修改）
const initSearchParams = {
  current: 1,
  pageSize: 10,
};

const form = ref<API.AppUpdateRequest>({
  id: undefined,
  appName: "",
  appDesc: "",
  appIcon: "",
  appType: undefined,
  scoringStrategy: undefined,
});
const rules = {
  id: [{ required: true, message: "请输入ID" }],
  appName: [
    { required: true, message: "请输入应用名称" },
    { max: 80, message: "最大长度为80" },
  ],
  appDesc: [{ required: true, message: "请输入应用描述" }],
  appType: [{ required: true, message: "请选择应用类型" }],
  scoringStrategy: [{ required: true, message: "请选择评分策略" }],
};
const formRef = ref();
const visible = ref(false);
const handleOk = async () => {
  const res = await updateAppUsingPost(form.value);
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
const getAppDetail = async (id: number) => {
  const res = await getAppVoByIdUsingGet({
    id,
  });
  if (res.data.code === 0) {
    form.value = res.data.data;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
  visible.value = true;
};

const rejectVisible = ref(false);
const rejectFormRef = ref();
const rejectForm = ref({
  id: undefined,
  reviewStatus: REVIEW_STATUS_ENUM.REJECT,
  reviewMessage: "",
});
const handleRejectCancel = () => {
  rejectForm.value.id = undefined;
  rejectForm.value.reviewStatus = REVIEW_STATUS_ENUM.REJECT
  rejectForm.value.reviewMessage = "";
  rejectFormRef.value.clearValidate();
  rejectVisible.value = false;
};

const searchParams = ref<API.AppQueryRequest>({
  ...initSearchParams,
});
const dataList = ref<API.App[]>([]);
const total = ref<number>(0);

/**
 * 加载数据
 */
const loadData = async () => {
  const res = await listAppByPageUsingPost(searchParams.value);
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
const doDelete = async (record: API.App) => {
  if (!record.id) {
    return;
  }

  const res = await deleteAppUsingPost({
    id: record.id,
  });
  if (res.data.code === 0) {
    loadData();
  } else {
    message.error("删除失败，" + res.data.message);
  }
};

/**
 * 审核
 */
const doReview = async () => {
  const res = await doAppReviewUsingPost(rejectForm.value);
  if (res.data.code === 0) {
    message.success("审核成功");
    loadData();
  } else {
    message.error("审核失败，" + res.data.message);
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
    title: "名称",
    dataIndex: "appName",
    width: 150,
    align: "center",
  },
  {
    title: "描述",
    dataIndex: "appDesc",
    ellipsis: true,
    tooltip: true,
    width: 200,
    align: "center",
  },
  {
    title: "图标",
    dataIndex: "appIcon",
    slotName: "appIcon",
    width: 100,
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
    title: "审核状态",
    dataIndex: "reviewStatus",
    slotName: "reviewStatus",
    width: 90,
    align: "center",
  },
  {
    title: "审核信息",
    dataIndex: "reviewMessage",
    ellipsis: true,
    tooltip: true,
    width: 200,
    align: "center",
  },
  {
    title: "审核时间",
    dataIndex: "reviewTime",
    slotName: "reviewTime",
    width: 200,
    align: "center",
    sortable: {
      sortDirections: ["ascend", "descend"],
    },
  },
  {
    title: "审核人 id",
    dataIndex: "reviewerId",
    ellipsis: true,
    tooltip: true,
    width: 100,
    align: "center",
  },
  {
    title: "审核人名称",
    dataIndex: "reviewerUserName",
    width: 120,
    align: "center",
  },
  {
    title: "创建用户 id",
    dataIndex: "userId",
    ellipsis: true,
    tooltip: true,
    width: 120,
    align: "center",
  },
  {
    title: "创建用户名称",
    dataIndex: "createUserName",
    width: 120,
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
