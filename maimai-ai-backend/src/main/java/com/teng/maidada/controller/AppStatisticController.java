package com.teng.maidada.controller;

import com.teng.maidada.common.BaseResponse;
import com.teng.maidada.common.ErrorCode;
import com.teng.maidada.common.ResultUtils;
import com.teng.maidada.exception.ThrowUtils;
import com.teng.maidada.mapper.UserAnswerMapper;
import com.teng.maidada.model.vo.AppAnswerCountVO;
import com.teng.maidada.model.vo.AppAnswerResultCountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/6/2 21:05
 */
@RestController
@RequestMapping("/app/statistic")
@Api(tags = "应用统计接口")
@Slf4j
public class AppStatisticController {
    @Resource
    private UserAnswerMapper userAnswerMapper;

    @ApiOperation("获取TOP K热门应用统计信息")
    @GetMapping("/answer_count")
    public BaseResponse<List<AppAnswerCountVO>> getAppAnswerCount(Integer value) {
        if (value == null || value <= 0) {
            value = 5;
        }
        return ResultUtils.success(userAnswerMapper.getAppAnswerCount(value));
    }

    @ApiOperation("获取应用回答分布统计")
    @GetMapping("/answer_result_count")
    public BaseResponse<List<AppAnswerResultCountVO>> getAppAnswerResultCount(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId 非法");
        return ResultUtils.success(userAnswerMapper.getAppAnswerResultCount(appId));
    }
}
