package com.teng.maidada.model.vo;

import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/6/2 21:06
 */
@Data
public class AppAnswerCountVO {
    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用答题数量
     */
    private Long answerCount;
}
