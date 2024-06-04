// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 创建评分结果 POST /api/scoringResult/add */
export async function addScoringResultUsingPost(
  body: API.ScoringResultAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/scoringResult/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除评分结果 POST /api/scoringResult/delete */
export async function deleteScoringResultUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/scoringResult/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 编辑评分结果（给用户使用） POST /api/scoringResult/edit */
export async function editScoringResultUsingPost(
  body: API.ScoringResultEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/scoringResult/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 根据 id 获取评分结果（封装类） GET /api/scoringResult/get/vo */
export async function getScoringResultVoByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getScoringResultVOByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseScoringResultVO_>('/api/scoringResult/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 分页获取评分结果列表（仅管理员可用） POST /api/scoringResult/list/page */
export async function listScoringResultByPageUsingPost(
  body: API.ScoringResultQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageScoringResult_>('/api/scoringResult/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取评分结果列表（封装类） POST /api/scoringResult/list/page/vo */
export async function listScoringResultVoByPageUsingPost(
  body: API.ScoringResultQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageScoringResultVO_>('/api/scoringResult/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取当前登录用户创建的评分结果列表 POST /api/scoringResult/my/list/page/vo */
export async function listMyScoringResultVoByPageUsingPost(
  body: API.ScoringResultQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageScoringResultVO_>('/api/scoringResult/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新评分结果（仅管理员可用） POST /api/scoringResult/update */
export async function updateScoringResultUsingPost(
  body: API.ScoringResultUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/scoringResult/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
