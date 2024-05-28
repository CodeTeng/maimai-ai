package com.teng.maidada.scoring;

import com.teng.maidada.model.entity.App;
import com.teng.maidada.model.entity.UserAnswer;

import java.util.List;

/**
 * 评分策略
 *
 * @author 程序员麦麦
 *
 */
public interface ScoringStrategy {

    /**
     * 执行评分
     *
     * @param choices
     * @param app
     * @return
     * @throws Exception
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}