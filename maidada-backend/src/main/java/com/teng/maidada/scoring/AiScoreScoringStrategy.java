package com.teng.maidada.scoring;

import com.teng.maidada.model.entity.App;
import com.teng.maidada.model.entity.UserAnswer;

import java.util.List;

/**
 * @description: AI 得分类应用评分策略
 * @author: ~Teng~
 * @date: 2024/5/28 18:04
 */
@ScoringStrategyConfig(appType = 0, scoringStrategy = 1)
public class AiScoreScoringStrategy implements ScoringStrategy {
    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        return null;
    }
}
