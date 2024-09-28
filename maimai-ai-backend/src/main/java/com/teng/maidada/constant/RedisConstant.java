package com.teng.maidada.constant;

/**
 * @description: Redis 键的 key
 * @author: ~Teng~
 * @date: 2024/5/30 14:23
 */
public interface RedisConstant {
    /**
     * AI 测评类锁
     */
    String AI_TEST_ANSWER_LOCK = "AI_ANSWER_LOCK";

    /**
     * AI 得分类类锁
     */
    String AI_SCORE_ANSWER_LOCK = "AI_ANSWER_LOCK";

    /**
     * 用户注册账号锁
     */
    String USER_ACCOUNT_LOCK = "USER_ACCOUNT_LOCK";
}
