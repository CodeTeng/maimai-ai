package com.teng.maidada.model.dto.question;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionContentDTO {

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目选项列表
     */
    private List<Option> options;

    /**
     * 题目选项
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {
        /**
         * 如果是测评类 result 保存答案属性
         */
        private String result;

        /**
         * 如果是得分类 score 设置本题分数
         */
        private Integer score;

        /**
         * 选型内容
         */
        private String value;

        /**
         * 选型内容对应的 key
         */
        private String key;
    }
}


