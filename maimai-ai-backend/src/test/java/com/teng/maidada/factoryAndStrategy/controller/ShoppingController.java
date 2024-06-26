package com.teng.maidada.factoryAndStrategy.controller;

import com.teng.maidada.common.BaseResponse;
import com.teng.maidada.common.ErrorCode;
import com.teng.maidada.common.ResultUtils;
import com.teng.maidada.factoryAndStrategy.service.ShoppingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: 购物控制器
 * @author: ~Teng~
 * @date: 2024/6/26 18:23
 */
@Slf4j
@RestController
@RequestMapping("/shopping")
public class ShoppingController {
    @Resource(name = "shoppingService1")
    private ShoppingService shoppingService1;

    @Resource(name = "shoppingService2")
    private ShoppingService shoppingService2;

    @Resource(name = "shoppingService3")
    private ShoppingService shoppingService3;

    /**
     * 购买商品
     *
     * @param userId        用户ID
     * @param goodsId 商品ID
     * @return 购买结果
     */
    @GetMapping("/buy")
    public BaseResponse<?> buy(Long userId, Long goodsId) {
        log.info("user id: {}, merchandise id: {}", userId, goodsId);
        if (userId == null || goodsId == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(shoppingService1.buy(userId, goodsId));
    }
}
