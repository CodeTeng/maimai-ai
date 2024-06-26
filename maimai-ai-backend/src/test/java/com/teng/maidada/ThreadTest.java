package com.teng.maidada;

import com.teng.maidada.controller.QuestionController;
import com.teng.maidada.model.dto.question.AiGenerateQuestionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/6/2 20:56
 */
@SpringBootTest
public class ThreadTest {
    @Resource
    private QuestionController questionController;

    @Test
    void test() throws InterruptedException {
        AiGenerateQuestionRequest aiGenerateQuestionRequest = new AiGenerateQuestionRequest();
        aiGenerateQuestionRequest.setAppId(3L);
        aiGenerateQuestionRequest.setQuestionNumber(10);
        aiGenerateQuestionRequest.setOptionNumber(4);
//        questionController.aiGenerateQuestionSSETest(aiGenerateQuestionRequest, false);
//        questionController.aiGenerateQuestionSSETest(aiGenerateQuestionRequest, false);
//        questionController.aiGenerateQuestionSSETest(aiGenerateQuestionRequest, true);
        Thread.sleep(1000000L);
    }
}
