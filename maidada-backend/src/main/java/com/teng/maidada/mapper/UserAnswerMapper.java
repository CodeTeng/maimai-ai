package com.teng.maidada.mapper;

import com.teng.maidada.model.entity.UserAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teng.maidada.model.vo.AppAnswerCountVO;
import com.teng.maidada.model.vo.AppAnswerResultCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 程序员麦麦
 * @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
 * @createDate 2024-05-09 20:41:03
 * @Entity com.teng.maidada.model.entity.UserAnswer
 */
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {
    /**
     * 获取TOP K热门应用统计信息
     */
    List<AppAnswerCountVO> getAppAnswerCount(@Param("value") Integer value);

    /**
     * 获取应用回答分布统计
     */
    List<AppAnswerResultCountVO> getAppAnswerResultCount(@Param("appId") Long appId);
}




