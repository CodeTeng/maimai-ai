package com.teng.maidada.model.dto.question;

import lombok.Data;

/**
 * 题目答案封装类（用于 AI 评分）
 *
 * @author 程序员麦麦
 * 
 */
@Data
public class QuestionAnswerDTO {

    /**
     * 题目
     */
    private String title;

    /**
     * 用户答案
     */
    private String userAnswer;
}
