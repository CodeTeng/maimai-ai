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
import com.teng.maidada.model.dto.question.QuestionAnswerDTO;
import com.teng.maidada.model.dto.question.QuestionContentDTO;
import com.teng.maidada.model.entity.App;
import com.teng.maidada.model.entity.Question;
import com.teng.maidada.model.entity.UserAnswer;
import com.teng.maidada.model.vo.QuestionVO;
import com.teng.maidada.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private RedissonClient redissonClient;

    /**
     * 本地缓存
     */
    private final Cache<String, String> answerCacheMap = Caffeine.newBuilder().initialCapacity(1024)
            // 缓存1天移除
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
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
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

        // 锁要细粒度
        RLock lock = redissonClient.getLock(RedisConstant.AI_ANSWER_LOCK + cacheKey);
        try {
            // 竞争分布式锁 等待 3秒 15秒后自动释放
            if (!lock.tryLock(3, 15, TimeUnit.SECONDS)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "请稍后再试");
            }
            // 1. 根据 id 查询到题目
            Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId));
            QuestionVO questionVO = QuestionVO.objToVo(question);
            List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

            // 2. 调用 AI 获取结果
            // 封装 Prompt
            String userMessage = getAiTestScoringUserMessage(app, questionContent, choices);
            // AI 生成
            String result = aiManager.doSyncStableRequest(PromptConstant.AI_TEST_SCORING_SYSTEM_MESSAGE, userMessage);
            // 截取需要的 JSON 信息
            int start = result.indexOf("{");
            int end = result.lastIndexOf("}");
            String json = result.substring(start, end + 1);

            // 3. 缓存 AI 结果
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
                    log.info("解锁成功");
                    lock.unlock();
                }
            }
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
