// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 获取TOP K热门应用统计信息 GET /api/app/statistic/answer_count */
export async function getAppAnswerCountUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAppAnswerCountUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListAppAnswerCountVO_>(
    "/api/app/statistic/answer_count",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 获取应用回答分布统计 GET /api/app/statistic/answer_result_count */
export async function getAppAnswerResultCountUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAppAnswerResultCountUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListAppAnswerResultCountVO_>(
    "/api/app/statistic/answer_result_count",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
