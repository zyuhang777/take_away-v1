package com.zyh.spring.take_away.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.spring.take_away.commen.R;
import com.zyh.spring.take_away.pojo.ShoppingCart;
import com.zyh.spring.take_away.service.ShoppingCartService;
import com.zyh.spring.take_away.util.ThreadLocalContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Api(tags = "用户购物车控制层")
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @ApiOperation("得到用户购物车列表")
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, ThreadLocalContext.get());
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return R.success(list);
    }

    @ApiOperation("购物车添加菜品或套餐")
    @PostMapping("/add")
    public R<String> addDishOrSetmeal(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(ThreadLocalContext.get());
        ShoppingCart shoppingCartDb = shoppingCartService.selectDishOrSetmeal(shoppingCart);
        String tip;
        if (shoppingCartDb == null) {
            shoppingCartService.save(shoppingCart);
            tip = shoppingCart.getDishId() == null ? "套餐添加成功" : "菜品添加成功";
        } else {
            shoppingCartDb.setNumber(shoppingCartDb.getNumber() + 1);
            shoppingCartService.updateById(shoppingCartDb);
            tip = shoppingCart.getDishId() == null ? "套餐增加成功" : "菜品增加成功";
        }


        return R.success(tip);
    }

    @ApiOperation("购物车的中减去菜品或套餐")
    @PostMapping("/sub")
    public R<String> subDishOrSetmeal(@RequestBody ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (dishId!=null){
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }
        ShoppingCart shoppingCartDb = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        Integer number = shoppingCartDb.getNumber();
        if (number > 1) {
            shoppingCartDb.setNumber(shoppingCartDb.getNumber() - 1);
            shoppingCartService.updateById(shoppingCartDb);
        } else {
            shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        }
        return R.success("购物车更改成功");
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public R<String> cleanShoppingCart() {
        Long userID = ThreadLocalContext.get();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, userID);
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        return R.success("购物车清空成功");
    }
}
