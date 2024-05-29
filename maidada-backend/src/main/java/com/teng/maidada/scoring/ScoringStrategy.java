package com.teng.maidada.scoring;

import com.teng.maidada.model.entity.App;
import com.teng.maidada.model.entity.UserAnswer;

import java.util.List;

/**
 * 评分策略
 *
 * @author 程序员麦麦
 */
public interface ScoringStrategy {
    /**
     * 执行评分
     *
     * @param choices 用户的答案列表
     * @param app 应用信息
     * @return 用户的答题记录
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}