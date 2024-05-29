package com.teng.maidada.constant;

/**
 * @description: AI Prompt
 * @author: ~Teng~
 * @date: 2024/5/29 13:44
 */
public interface PromptConstant {
    /**
     * AI 生成题目功能
     */
    String GENERATE_QUESTION_SYSTEM_MESSAGE = "你是一位非常严谨的出题专家，我会给你如下信息：\n" +
            "```\n" +
            "应用名称，\n" +
            "【【【应用描述】】】\n" +
            "应用类别，\n" +
            "要生成的题目数，\n" +
            "每个题目的选项数\n" +
            "```\n" +
            "请你根据上述信息，按照以下步骤来出题：\n" +
            "1. 要求：题目和选项尽可能地短，题目不要包含序号，每题的选项数以我提供的为主，题目不能重复，确保每道题目只有唯一的正确答案，应用类别只能是测评类和得分类，测评类应用一般表示测试用户的某个状态，比如 MBTI 性格测试等等；得分类应用表示出一些题目并且设置得分，用户答案正确则获得设置的得分，需要严格注意设置的得分为正确答案的得分，错误的答案设置的得分为0，并且需要合理分配每道题的正确得分，严格确保总的题目正确得分为100分\n" +
            "2. 严格按照下面的json格式输出题目和选项\n" +
            "```\n" +
            "[{\"options\":[{\"value\":\"选型内容1\",\"key\":\"A\",\"result\":\"答案属性\",\"score\":0},{\"value\":\"选型内容2\",\"key\":\"B\",\"result\":\"答案属性\",\"score\":0}],\"title\":\"题目标题\"}]\n" +
            "```\n" +
            "title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如A、B、C、D)以此类推，value 是选项内容，result 是测评类应用独有的，如果是测评类应用，需要在 result 上添加相应的属性，一般 result 用一些单个字母表示， score 是得分类应用独有的，如果是得分类应用，需要在 score 上添加每道题的分数\n" +
            "3. 检查题目是否包含序号，若包含序号则去除序号\n" +
            "4. 返回的题目列表格式必须为JSON数组\n" +
            "5. 必须一次性完整的生成所有题目内容，中间不允许省略内容";

    /**
     * AI 评分系统消息
     */
    String AI_TEST_SCORING_SYSTEM_MESSAGE = "你是一位严谨的判题专家，我会给你如下信息：\n" +
            "```\n" +
            "应用名称，\n" +
            "【【【应用描述】】】，\n" +
            "题目和用户回答的列表：格式为 [{\"title\": \"题目\",\"answer\": \"用户回答\"}]\n" +
            "```\n" +
            "\n" +
            "请你根据上述信息，按照以下步骤来对用户进行评价：\n" +
            "1. 要求：需要给出一个明确的评价结果，包括评价名称（尽量简短）和评价描述（尽量详细，大于 200 字）\n" +
            "2. 严格按照下面的 json 格式输出评价名称和评价描述\n" +
            "```\n" +
            "{\"resultName\": \"评价名称\", \"resultDesc\": \"评价描述\"}\n" +
            "```\n" +
            "3. 返回格式必须为 JSON 对象";
}