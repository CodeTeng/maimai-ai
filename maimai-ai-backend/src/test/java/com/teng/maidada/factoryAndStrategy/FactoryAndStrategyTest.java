package com.teng.maidada.factoryAndStrategy;

import com.alibaba.fastjson.JSON;
import com.teng.maidada.factoryAndStrategy.service.ShoppingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/6/26 18:56
 */
@SpringBootTest
@Slf4j
public class FactoryAndStrategyTest {
    @Resource(name = "shoppingService1")
    private ShoppingService shoppingService1;

    @Resource(name = "shoppingService2")
    private ShoppingService shoppingService2;

    @Resource(name = "shoppingService3")
    private ShoppingService shoppingService3;

    @Test
    void test1() {
        Map<String, Object> objectMap1 = shoppingService1.buy(1L, 1L);
        Map<String, Object> objectMap2 = shoppingService1.buy(2L, 2L);
        Map<String, Object> objectMap3 = shoppingService1.buy(3L, 3L);
        Map<String, Object> objectMap4 = shoppingService1.buy(4L, 4L);
        Map<String, Object> objectMap5 = shoppingService1.buy(5L, 5L);
        log.info("返回结果：{}", JSON.toJSONString(objectMap1));
        log.info("返回结果：{}", JSON.toJSONString(objectMap2));
        log.info("返回结果：{}", JSON.toJSONString(objectMap3));
        log.info("返回结果：{}", JSON.toJSONString(objectMap4));
        log.info("返回结果：{}", JSON.toJSONString(objectMap5));
    }

    @Test
    void test2() {
        Map<String, Object> objectMap1 = shoppingService2.buy(1L, 1L);
        Map<String, Object> objectMap2 = shoppingService2.buy(2L, 2L);
        Map<String, Object> objectMap3 = shoppingService2.buy(3L, 3L);
        Map<String, Object> objectMap4 = shoppingService2.buy(4L, 4L);
        Map<String, Object> objectMap5 = shoppingService2.buy(5L, 5L);
        log.info("返回结果：{}", JSON.toJSONString(objectMap1));
        log.info("返回结果：{}", JSON.toJSONString(objectMap2));
        log.info("返回结果：{}", JSON.toJSONString(objectMap3));
        log.info("返回结果：{}", JSON.toJSONString(objectMap4));
        log.info("返回结果：{}", JSON.toJSONString(objectMap5));
    }

    @Test
    void test3() {
        Map<String, Object> objectMap1 = shoppingService3.buy(1L, 1L);
        Map<String, Object> objectMap2 = shoppingService3.buy(2L, 2L);
        Map<String, Object> objectMap3 = shoppingService3.buy(3L, 3L);
        Map<String, Object> objectMap4 = shoppingService3.buy(4L, 4L);
        Map<String, Object> objectMap5 = shoppingService3.buy(5L, 5L);
        log.info("返回结果：{}", JSON.toJSONString(objectMap1));
        log.info("返回结果：{}", JSON.toJSONString(objectMap2));
        log.info("返回结果：{}", JSON.toJSONString(objectMap3));
        log.info("返回结果：{}", JSON.toJSONString(objectMap4));
        log.info("返回结果：{}", JSON.toJSONString(objectMap5));
    }
}
