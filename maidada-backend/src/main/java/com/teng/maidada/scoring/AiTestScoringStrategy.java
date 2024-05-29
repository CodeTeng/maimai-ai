package com.teng.maidada.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.teng.maidada.common.ErrorCode;
import com.teng.maidada.constant.PromptConstant;
import com.teng.maidada.exception.BusinessException;
import com.teng.maidada.manager.AiManager;
import com.teng.maidada.model.dto.question.QuestionAnswerDTO;
import com.teng.maidada.model.dto.question.QuestionContentDTO;
import com.teng.maidada.model.entity.App;
import com.teng.maidada.model.entity.Question;
import com.teng.maidada.model.entity.UserAnswer;
import com.teng.maidada.model.vo.QuestionVO;
import com.teng.maidada.service.QuestionService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * AI 测评类应用评分策略
 *
 * @author 程序员麦麦
 */
@ScoringStrategyConfig(appType = 1, scoringStrategy = 1)
@Slf4j
public class AiTestScoringStrategy implements ScoringStrategy {
    @Resource
    private QuestionService questionService;

    @Resource
    private AiManager aiManager;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Long appId = app.getId();
        // 1. 根据 id 查询到题目
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId));
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

        // 2. 调用 AI 获取结果
        // 封装 Prompt
        String userMessage = getAiTestScoringUserMessage(app, questionContent, choices);
        // AI 生成
        try {
            String result = aiManager.doSyncStableRequest(PromptConstant.AI_TEST_SCORING_SYSTEM_MESSAGE, userMessage);
            // 截取需要的 JSON 信息
            int start = result.indexOf("{");
            int end = result.lastIndexOf("}");
            String json = result.substring(start, end + 1);

            // 3. 构造返回值，填充答案对象的属性
            UserAnswer userAnswer = JSONUtil.toBean(json, UserAnswer.class);
            userAnswer.setAppId(appId);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(JSONUtil.toJsonStr(choices));
            return userAnswer;
        } catch (Exception e) {
            log.error("AI判题失败");
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI判题失败");
        }
    }

    /**
     * AI 评分用户消息封装
     */
    private String getAiTestScoringUserMessage(App app, List<QuestionContentDTO> questionContentDTOList, List<String> choices) {
        StringBuilder userMessage = new StringBuilder();
        userMessage.append(app.getAppName()).append("\n");
        userMessage.append("【【【" + app.getAppDesc() + "】】】").append("\n");
        List<QuestionAnswerDTO> questionAnswerDTOList = new ArrayList<>();
        for (int i = 0; i < questionContentDTOList.size(); i++) {
            QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
            questionAnswerDTO.setTitle(questionContentDTOList.get(i).getTitle());
            questionAnswerDTO.setUserAnswer(choices.get(i));
            questionAnswerDTOList.add(questionAnswerDTO);
        }
        userMessage.append(JSONUtil.toJsonStr(questionAnswerDTOList));
        return userMessage.toString();
    }
}
