package com.teng.maidada.scoring;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.teng.maidada.common.ErrorCode;
import com.teng.maidada.constant.PromptConstant;
import com.teng.maidada.constant.RedisConstant;
import com.teng.maidada.exception.BusinessException;
import com.teng.maidada.manager.AiManager;
import com.teng.maidada.model.dto.question.QuestionAIAnswerDTO;
import com.teng.maidada.model.dto.question.QuestionContentDTO;
import com.teng.maidada.model.entity.App;
import com.teng.maidada.model.entity.Question;
import com.teng.maidada.model.entity.UserAnswer;
import com.teng.maidada.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description: AI 得分类应用评分策略
 * @author: ~Teng~
 * @date: 2024/5/28 18:04
 */
@ScoringStrategyConfig(appType = 0, scoringStrategy = 1)
@Slf4j
public class AiScoreScoringStrategy implements ScoringStrategy {
    @Resource
    private QuestionService questionService;

    @Resource
    private AiManager aiManager;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 本地缓存
     */
    private final Cache<String, String> answerCacheMap = Caffeine.newBuilder()
            .initialCapacity(1024)
            .expireAfterAccess(1L, TimeUnit.DAYS)
            .build();

    /**
     * 构建缓存的 key
     * @param appId 应用 id
     * @param choiceStr 用户的答案
     * @return 缓存 key
     */
    private String buildCacheKey(Long appId, String choiceStr) {
        return DigestUtil.md5Hex(appId + ":" + choiceStr);
    }

    @Override
    public UserAnswer doScore(List<String> choices, App app) {
        Long appId = app.getId();
        // 0. 命中缓存 直接返回结果
        String choiceStr = JSONUtil.toJsonStr(choices);
        String cacheKey = buildCacheKey(appId, choiceStr);
        String answerJson = answerCacheMap.getIfPresent(cacheKey);
        if (StringUtils.isNoneBlank(answerJson)) {
            UserAnswer userAnswer = JSONUtil.toBean(answerJson, UserAnswer.class);
            userAnswer.setAppId(appId);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(choiceStr);
            return userAnswer;
        }

        // 1. 根据 id 查询到题目
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
        );

        RLock lock = redissonClient.getLock(RedisConstant.AI_SCORE_ANSWER_LOCK);
        try {
            // 竞争分布式锁 等待 3秒 15秒后自动释放
            if (!lock.tryLock(3, 15, TimeUnit.SECONDS)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "请稍后再试");
            }
            // 2. 调用 AI 获取结果
            // 封装 Prompt
            String userMessage = getAiScoreScoringUserMessage(app, question, choices);
            // AI 生成
            String result = aiManager.doSyncStableRequest(PromptConstant.AI_SCORE_SCORING_SYSTEM_MESSAGE, userMessage);
            // 3. 解析 AI 返回结果
            int start = result.indexOf("{");
            int end = result.lastIndexOf("}");
            String json = result.substring(start, end + 1);

            // 缓存 AI 结果
            answerCacheMap.put(cacheKey, json);

            // 4. 构造返回值，填充答案对象的属性
            UserAnswer userAnswer = JSONUtil.toBean(json, UserAnswer.class);
            userAnswer.setAppId(appId);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(JSONUtil.toJsonStr(choices));
            return userAnswer;
        } catch (Exception e) {
            log.error("AI判题失败");
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI判题失败");
        } finally {
            if (lock != null && lock.isLocked()) {
                if (lock.isHeldByCurrentThread()) {
                    log.info(Thread.currentThread().getName() + " 解锁成功");
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 生成用户信息
     */
    private String getAiScoreScoringUserMessage(App app, Question question, List<String> choices) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(app.getAppName() + "\n");
        stringBuilder.append("【【【" + app.getAppDesc() + "】】】\n");
        QuestionAIAnswerDTO questionAIAnswerDTO = new QuestionAIAnswerDTO();
        questionAIAnswerDTO.setAnswer(choices);
        String questionContent = question.getQuestionContent();
        List<QuestionContentDTO> questionContentDTOS = JSONUtil.toList(questionContent, QuestionContentDTO.class);
        questionAIAnswerDTO.setQuestion(questionContentDTOS);
        stringBuilder.append(JSONUtil.toJsonStr(questionAIAnswerDTO));
        return stringBuilder.toString();
    }
}
