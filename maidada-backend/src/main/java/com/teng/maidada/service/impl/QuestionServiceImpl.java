package com.teng.maidada.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.maidada.common.ErrorCode;
import com.teng.maidada.constant.CommonConstant;
import com.teng.maidada.exception.ThrowUtils;
import com.teng.maidada.mapper.QuestionMapper;
import com.teng.maidada.model.dto.question.QuestionContentDTO;
import com.teng.maidada.model.dto.question.QuestionQueryRequest;
import com.teng.maidada.model.entity.App;
import com.teng.maidada.model.entity.Question;
import com.teng.maidada.model.entity.User;
import com.teng.maidada.model.enums.AppTypeEnum;
import com.teng.maidada.model.vo.QuestionVO;
import com.teng.maidada.model.vo.UserVO;
import com.teng.maidada.service.AppService;
import com.teng.maidada.service.QuestionService;
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
 * 题目服务实现
 *
 * @author 程序员麦麦
 *
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;

    /**
     * 校验数据
     *
     * @param question 题目
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        ThrowUtils.throwIf(question == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String questionContent = question.getQuestionContent();
        Long appId = question.getAppId();
        Long id = question.getId();
        ThrowUtils.throwIf(StringUtils.isBlank(questionContent), ErrorCode.PARAMS_ERROR, "题目内容不能为空");
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId 非法");
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR, "应用不存在");
        if (!add) {
            // 修改题目 补充校验规则
            ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "id 非法");
        }
    }

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = questionQueryRequest.getId();
        String questionContent = questionQueryRequest.getQuestionContent();
        Long appId = questionQueryRequest.getAppId();
        Long userId = questionQueryRequest.getUserId();
        Long notId = questionQueryRequest.getNotId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(questionContent), "questionContent", questionContent);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appId), "appId", appId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        if (!SqlUtils.validSortField(sortField)) {
            queryWrapper.orderByDesc("createTime");
        } else {
            queryWrapper.orderBy(true, sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        }
        return queryWrapper;
    }

    /**
     * 获取题目封装
     *
     * @param question
     * @return
     */
    @Override
    public QuestionVO getQuestionVO(Question question) {
        // 对象转封装类
        QuestionVO questionVO = QuestionVO.objToVo(question);

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUser(userVO);
        return questionVO;
    }

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionVO> questionVOList = questionList.stream().map(QuestionVO::objToVo).collect(Collectors.toList());
        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userService.listByIds(userIdSet).stream().collect(Collectors.toMap(User::getId, u -> u));
        // 填充信息
        questionVOList.forEach(questionVO -> {
            Long userId = questionVO.getUserId();
            User user = userMap.getOrDefault(userId, null);
            questionVO.setUser(userService.getUserVO(user));
        });
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    public void validaQuestionContent(List<QuestionContentDTO> questionContentDTOs, Long appId) {
        App app = appService.getById(appId);
        Integer appType = app.getAppType();
        questionContentDTOs.forEach(questionContentDTO -> {
            String title = questionContentDTO.getTitle();
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR, "题目标题不能为空");
            List<QuestionContentDTO.Option> options = questionContentDTO.getOptions();
            ThrowUtils.throwIf(CollUtil.isEmpty(options), ErrorCode.PARAMS_ERROR, "选项列表不能为空");
            options.forEach(option -> {
                ThrowUtils.throwIf(ObjectUtils.isEmpty(option), ErrorCode.PARAMS_ERROR, "选项不能为空");
                String key = option.getKey();
                ThrowUtils.throwIf(StringUtils.isBlank(key), ErrorCode.PARAMS_ERROR, "选项 key 不能为空");
                String value = option.getValue();
                ThrowUtils.throwIf(StringUtils.isBlank(value), ErrorCode.PARAMS_ERROR, "选项内容不能为空");
                if (appType.equals(AppTypeEnum.TEST.getValue())) {
                    // 测评类 result 必须存在
                    String result = option.getResult();
                    ThrowUtils.throwIf(StringUtils.isBlank(result), ErrorCode.PARAMS_ERROR, "result 非法");
                } else if (appType.equals(AppTypeEnum.SCORE.getValue())) {
                    // 得分类 score 必须存在
                    Integer score = option.getScore();
                    ThrowUtils.throwIf(score == null || score < 0, ErrorCode.PARAMS_ERROR, "score 非法");
                }
            });
        });
    }

}
