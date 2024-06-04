package com.teng.maidada.model.vo;

import lombok.Data;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/6/2 21:15
 */
@Data
public class AppAnswerResultCountVO {
    /**
     * 结果名称
     */
    private String resultName;

    /**
     * 结果个数
     */
    private Long resultCount;
}
