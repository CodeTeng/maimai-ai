package com.teng.maidada.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.maidada.common.ErrorCode;
import com.teng.maidada.constant.CommonConstant;
import com.teng.maidada.exception.ThrowUtils;
import com.teng.maidada.mapper.ScoringResultMapper;
import com.teng.maidada.model.dto.scoringResult.ScoringResultQueryRequest;
import com.teng.maidada.model.entity.App;
import com.teng.maidada.model.entity.ScoringResult;
import com.teng.maidada.model.entity.User;
import com.teng.maidada.model.enums.AppTypeEnum;
import com.teng.maidada.model.vo.ScoringResultVO;
import com.teng.maidada.model.vo.UserVO;
import com.teng.maidada.service.AppService;
import com.teng.maidada.service.ScoringResultService;
import com.teng.maidada.service.UserService;
import com.teng.maidada.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 评分结果服务实现
 *
 * @author 程序员麦麦
 *
 */
@Service
@Slf4j
public class ScoringResultServiceImpl extends ServiceImpl<ScoringResultMapper, ScoringResult> implements ScoringResultService {

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;

    /**
     * 校验数据
     *
     * @param scoringResult 评分结果
     * @param add           对创建的数据进行校验
     */
    @Override
    public void validScoringResult(ScoringResult scoringResult, boolean add) {
        ThrowUtils.throwIf(scoringResult == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String resultName = scoringResult.getResultName();
        Long appId = scoringResult.getAppId();
        // 创建数据时，参数不能为空
        ThrowUtils.throwIf(StringUtils.isBlank(resultName), ErrorCode.PARAMS_ERROR, "结果名称不能为空");
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId 非法");
        if (StringUtils.isNotBlank(resultName)) {
            ThrowUtils.throwIf(resultName.length() > 128, ErrorCode.PARAMS_ERROR, "结果名称不能超过 128");
        }
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR, "应用不存在");
        Integer appType = app.getAppType();
        String resultProp = scoringResult.getResultProp();
        if (appType.equals(AppTypeEnum.TEST.getValue())) {
            // 测评类 必须包含 resultProp
            ThrowUtils.throwIf(StringUtils.isBlank(resultProp), ErrorCode.PARAMS_ERROR, "测评类必须包含 resultProp");
        } else if (appType.equals(AppTypeEnum.SCORE.getValue())) {
            // 得分类 必须包含 resultScoreRange
            ThrowUtils.throwIf(scoringResult.getResultScoreRange() == null || scoringResult.getResultScoreRange() < 0, ErrorCode.PARAMS_ERROR, "得分类必须包含 resultScoreRange");
        }
        if (!add) {
            // 补充校验规则
            ThrowUtils.throwIf(scoringResult.getId() == null || scoringResult.getId() <= 0, ErrorCode.PARAMS_ERROR, "id 非法");
        }
    }

    /**
     * 获取查询条件
     */
    @Override
    public QueryWrapper<ScoringResult> getQueryWrapper(ScoringResultQueryRequest scoringResultQueryRequest) {
        QueryWrapper<ScoringResult> queryWrapper = new QueryWrapper<>();
        if (scoringResultQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = scoringResultQueryRequest.getId();
        String resultName = scoringResultQueryRequest.getResultName();
        String resultDesc = scoringResultQueryRequest.getResultDesc();
        String resultPicture = scoringResultQueryRequest.getResultPicture();
        String resultProp = scoringResultQueryRequest.getResultProp();
        Integer resultScoreRange = scoringResultQueryRequest.getResultScoreRange();
        Long appId = scoringResultQueryRequest.getAppId();
        Long userId = scoringResultQueryRequest.getUserId();
        Long notId = scoringResultQueryRequest.getNotId();
        String searchText = scoringResultQueryRequest.getSearchText();
        String sortField = scoringResultQueryRequest.getSortField();
        String sortOrder = scoringResultQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("resultName", searchText).or().like("resultDesc", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(resultName), "resultName", resultName);
        queryWrapper.like(StringUtils.isNotBlank(resultDesc), "resultDesc", resultDesc);
        queryWrapper.like(StringUtils.isNotBlank(resultProp), "resultProp", resultProp);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appId), "appId", appId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(resultScoreRange), "resultScoreRange", resultScoreRange);
        queryWrapper.eq(StringUtils.isNotBlank(resultPicture), "resultPicture", resultPicture);
        // 排序规则
        if (!SqlUtils.validSortField(sortField)) {
            queryWrapper.orderByDesc("createTime");
        } else {
            queryWrapper.orderBy(true, sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        }
        return queryWrapper;
    }

    /**
     * 获取评分结果封装
     */
    @Override
    public ScoringResultVO getScoringResultVO(ScoringResult scoringResult, HttpServletRequest request) {
        // 对象转封装类
        ScoringResultVO scoringResultVO = ScoringResultVO.objToVo(scoringResult);
        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // 关联查询用户信息
        Long userId = scoringResult.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        scoringResultVO.setUser(userVO);
        App app = appService.getById(scoringResult.getAppId());
        if (app != null) {
            scoringResultVO.setAppName(app.getAppName());
            scoringResultVO.setAppType(app.getAppType());
            scoringResultVO.setScoringStrategy(app.getScoringStrategy());
        }
        return scoringResultVO;
    }

    /**
     * 分页获取评分结果封装
     */
    @Override
    public Page<ScoringResultVO> getScoringResultVOPage(Page<ScoringResult> scoringResultPage) {
        List<ScoringResult> scoringResultList = scoringResultPage.getRecords();
        Page<ScoringResultVO> scoringResultVOPage = new Page<>(scoringResultPage.getCurrent(), scoringResultPage.getSize(), scoringResultPage.getTotal());
        if (CollUtil.isEmpty(scoringResultList)) {
            return scoringResultVOPage;
        }
        // 对象列表 => 封装对象列表
        List<ScoringResultVO> scoringResultVOList = scoringResultList.stream().map(ScoringResultVO::objToVo).collect(Collectors.toList());
        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // 关联查询用户信息
        Set<Long> userIdSet = scoringResultList.stream().map(ScoringResult::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userService.listByIds(userIdSet).stream().collect(Collectors.toMap(User::getId, u -> u));
        // 关联查询APP信息
        Set<Long> appIdSet = scoringResultList.stream().map(ScoringResult::getAppId).collect(Collectors.toSet());
        Map<Long, App> appMap = appService.listByIds(appIdSet).stream().collect(Collectors.toMap(App::getId, app -> app));
        // 填充信息
        scoringResultVOList.forEach(scoringResultVO -> {
            Long userId = scoringResultVO.getUserId();
            User user = userMap.getOrDefault(userId, null);
            scoringResultVO.setUser(userService.getUserVO(user));
            App app = appMap.getOrDefault(scoringResultVO.getAppId(), null);
            if (app != null) {
                scoringResultVO.setAppName(app.getAppName());
                scoringResultVO.setAppType(app.getAppType());
                scoringResultVO.setScoringStrategy(app.getScoringStrategy());
            }
        });
        scoringResultVOPage.setRecords(scoringResultVOList);
        return scoringResultVOPage;
    }

}
