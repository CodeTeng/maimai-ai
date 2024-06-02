package com.teng.maidada.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teng.maidada.annotation.AuthCheck;
import com.teng.maidada.common.BaseResponse;
import com.teng.maidada.common.DeleteRequest;
import com.teng.maidada.common.ErrorCode;
import com.teng.maidada.common.ResultUtils;
import com.teng.maidada.constant.PromptConstant;
import com.teng.maidada.constant.UserConstant;
import com.teng.maidada.exception.BusinessException;
import com.teng.maidada.exception.ThrowUtils;
import com.teng.maidada.manager.AiManager;
import com.teng.maidada.model.dto.question.*;
import com.teng.maidada.model.entity.App;
import com.teng.maidada.model.entity.Question;
import com.teng.maidada.model.entity.User;
import com.teng.maidada.model.enums.AppTypeEnum;
import com.teng.maidada.model.enums.UserRoleEnum;
import com.teng.maidada.model.vo.QuestionVO;
import com.teng.maidada.service.AppService;
import com.teng.maidada.service.QuestionService;
import com.teng.maidada.service.UserService;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 题目接口
 *
 * @author 程序员麦麦
 */
@RestController
@RequestMapping("/question")
@Slf4j
@Api(tags = "题目接口")
public class QuestionController {
    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;

    @Resource
    private AiManager aiManager;

    @Resource
    private Scheduler vipScheduler;

    @ApiOperation("创建题目")
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<QuestionContentDTO> questionContentDTOs = questionAddRequest.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContentDTOs));
        // 数据校验
        questionService.validQuestion(question, true);
        // 题目内容 JSON 结构进行校验
        Long appId = questionAddRequest.getAppId();
        questionService.validaQuestionContent(questionContentDTOs, appId);
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    @ApiOperation("删除题目")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @ApiOperation("更新题目（仅管理员可用）")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<QuestionContentDTO> questionContentDTO = questionUpdateRequest.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContentDTO));
        // 数据校验
        questionService.validQuestion(question, false);
        // 题目内容校验
        questionService.validaQuestionContent(questionContentDTO, questionUpdateRequest.getAppId());
        // 判断是否存在
        long id = questionUpdateRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @ApiOperation("根据 id 获取题目（封装类）")
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVO(question));
    }

    @ApiOperation("分页获取题目列表（仅管理员可用）")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size), questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    @ApiOperation("分页获取题目列表（封装类）")
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size), questionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    @ApiOperation("分页获取当前登录用户创建的题目列表")
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size), questionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    @ApiOperation("编辑题目（给用户使用）")
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<QuestionContentDTO> questionContentDTOS = questionEditRequest.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContentDTOS));
        // 数据校验
        questionService.validQuestion(question, false);
        // 校验题目内容
        Long appId = questionEditRequest.getAppId();
        questionService.validaQuestionContent(questionContentDTOS, appId);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = questionEditRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 生成题目的用户消息
     */
    private String getGenerateQuestionUserMessage(App app, int questionNumber, int optionNumber) {
        StringBuilder userMessage = new StringBuilder();
        userMessage.append(app.getAppName()).append("\n");
        userMessage.append("【【【" + app.getAppDesc() + "】】】").append("\n");
        userMessage.append(AppTypeEnum.getEnumByValue(app.getAppType()).getText()).append("\n");
        userMessage.append(questionNumber).append("\n");
        userMessage.append(optionNumber);
        return userMessage.toString();
    }

    @ApiOperation("AI生成同步")
    @PostMapping("/ai_generate")
    @Deprecated
    public BaseResponse<List<QuestionContentDTO>> aiGenerateQuestion(@RequestBody AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        ThrowUtils.throwIf(aiGenerateQuestionRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取参数
        Long appId = aiGenerateQuestionRequest.getAppId();
        Integer questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        Integer optionNumber = aiGenerateQuestionRequest.getOptionNumber();
        // 获取应用信息
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(questionNumber == null || questionNumber <= 0, ErrorCode.PARAMS_ERROR, "题目数量必须大于0");
        ThrowUtils.throwIf(questionNumber > 10, ErrorCode.PARAMS_ERROR, "题目数量一次生成不能超过10个");
        ThrowUtils.throwIf(optionNumber == null || optionNumber <= 0, ErrorCode.PARAMS_ERROR, "选项数量必须大于0");
        ThrowUtils.throwIf(optionNumber > 6, ErrorCode.PARAMS_ERROR, "选项数量不能超过6个");
        // 封装 Prompt
        String userMessage = getGenerateQuestionUserMessage(app, questionNumber, optionNumber);
        // AI 生成
        List<QuestionContentDTO> questionContentDTOList = null;
        try {
            String result = aiManager.doSyncStableRequest(PromptConstant.GENERATE_QUESTION_SYSTEM_MESSAGE, userMessage);
            // 截取需要的 JSON 信息
            int start = result.indexOf("[");
            int end = result.lastIndexOf("]");
            String json = result.substring(start, end + 1);
            questionContentDTOList = JSONUtil.toList(json, QuestionContentDTO.class);
        } catch (Exception e) {
            log.error("生成失败");
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "系统异常，生成失败");
        }
        return ResultUtils.success(questionContentDTOList);
    }

    @ApiOperation("AI生成 - SSE")
    @GetMapping("/ai_generate/sse")
    public SseEmitter aiGenerateQuestionSSE(AiGenerateQuestionRequest aiGenerateQuestionRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(aiGenerateQuestionRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取参数
        Long appId = aiGenerateQuestionRequest.getAppId();
        Integer questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        Integer optionNumber = aiGenerateQuestionRequest.getOptionNumber();
        // 获取应用信息
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(questionNumber == null || questionNumber <= 0, ErrorCode.PARAMS_ERROR, "题目数量必须大于0");
        ThrowUtils.throwIf(questionNumber > 10, ErrorCode.PARAMS_ERROR, "题目数量一次生成不能超过10个");
        ThrowUtils.throwIf(optionNumber == null || optionNumber <= 0, ErrorCode.PARAMS_ERROR, "选项数量必须大于0");
        ThrowUtils.throwIf(optionNumber > 6, ErrorCode.PARAMS_ERROR, "选项数量不能超过6个");
        // 封装 Prompt
        String userMessage = getGenerateQuestionUserMessage(app, questionNumber, optionNumber);
        User user = userService.getLoginUser(request);
        // AI 生成
        return questionService.aiGenerateQuestionSSE(userMessage, user);
    }

    @Deprecated
    @GetMapping("/ai_generate/sse/test")
    public SseEmitter aiGenerateQuestionSSETest(AiGenerateQuestionRequest aiGenerateQuestionRequest, boolean isVip) {
        ThrowUtils.throwIf(aiGenerateQuestionRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取参数
        Long appId = aiGenerateQuestionRequest.getAppId();
        Integer questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        Integer optionNumber = aiGenerateQuestionRequest.getOptionNumber();
        // 获取应用信息
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(questionNumber == null || questionNumber <= 0, ErrorCode.PARAMS_ERROR, "题目数量必须大于0");
        ThrowUtils.throwIf(questionNumber > 10, ErrorCode.PARAMS_ERROR, "题目数量一次生成不能超过10个");
        ThrowUtils.throwIf(optionNumber == null || optionNumber <= 0, ErrorCode.PARAMS_ERROR, "选项数量必须大于0");
        ThrowUtils.throwIf(optionNumber > 6, ErrorCode.PARAMS_ERROR, "选项数量不能超过6个");
        // 封装 Prompt
        String userMessage = getGenerateQuestionUserMessage(app, questionNumber, optionNumber);
        // AI 生成
        SseEmitter emitter = new SseEmitter(0L);
        AtomicInteger flag = new AtomicInteger(0);
        StringBuilder contentBuilder = new StringBuilder();
        Scheduler scheduler = Schedulers.single();
        // 这里可以修改为 VIP
        if (isVip) {
            scheduler = vipScheduler;
        }
        try {
            // 流式返回
            Flowable<ModelData> modelDataFlowable = aiManager.doStreamRequest(PromptConstant.GENERATE_QUESTION_SYSTEM_MESSAGE, userMessage, null);
            // 异步线程执行
            modelDataFlowable
                    .observeOn(scheduler)
                    .map(modelData -> modelData.getChoices().get(0).getDelta().getContent())
                    .map(content -> content.replaceAll("\\s", ""))
                    .filter(StringUtils::isNotBlank)
                    .flatMap(message -> {
                        // 将字符串转换为 List<Character>
                        List<Character> characterList = new ArrayList<>();
                        for (char c : message.toCharArray()) {
                            characterList.add(c);
                        }
                        return Flowable.fromIterable(characterList);
                    })
                    .doOnNext(c -> {
                        // 识别第一个 { 表示开始 AI 传输 JSON 数据，打开 flag 开始拼接 JSON 数组
                        if (c == '{') {
                            flag.addAndGet(1);
                        }
                        if (flag.get() > 0) {
                            contentBuilder.append(c);
                        }
                        if (c == '}') {
                            flag.addAndGet(-1);
                            if (flag.get() == 0) {
                                // 输出当前线程名字
                                System.out.println(Thread.currentThread().getName());
                                // 模拟普通用户阻塞
                                if (!isVip) {
                                    Thread.sleep(10000L);
                                }
                                // 累计单套题目满足 JSON 格式后， SSE 推送前端
                                // SSE 需要压缩成当行 JSON， SSE 无法识别换行
                                emitter.send(JSONUtil.toJsonStr(contentBuilder.toString()));
                                // 清空 StringBuilder
                                contentBuilder.setLength(0);
                            }
                        }
                    }).doOnComplete(emitter::complete).subscribe();
        } catch (Exception e) {
            log.error("生成失败");
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "系统异常，生成失败");
        }
        return emitter;
    }
}

















