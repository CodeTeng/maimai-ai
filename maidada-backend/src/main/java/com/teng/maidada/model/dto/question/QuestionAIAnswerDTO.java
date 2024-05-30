package com.teng.maidada.model.dto.question;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/5/30 18:21
 */
@Data
public class QuestionAIAnswerDTO {
    private List<QuestionContentDTO> question;

    private List<String> answer;
}
