package com.teng.maidada.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.teng.maidada.model.dto.question.QuestionContentDTO;
import com.teng.maidada.model.dto.question.QuestionQueryRequest;
import com.teng.maidada.model.entity.Question;
import com.teng.maidada.model.entity.User;
import com.teng.maidada.model.vo.QuestionVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题目服务
 *
 * @author 程序员麦麦
 *
 */
public interface QuestionService extends IService<Question> {

    /**
     * 校验数据
     *
     * @param question
     * @param add 对创建的数据进行校验
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @return
     */
    QuestionVO getQuestionVO(Question question);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    /**
     * 校验题目内容结构
     * @param questionContentDTOs
     * @param appId
     */
    void validaQuestionContent(List<QuestionContentDTO> questionContentDTOs, Long appId);

    /**
     * AI 生成 SSE
     * @param userMessage
     * @param loginUser
     * @return
     */
    SseEmitter aiGenerateQuestionSSE(String userMessage, User loginUser);
}
