package com.zyh.spring.take_away.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.spring.take_away.commen.R;
import com.zyh.spring.take_away.pojo.Orders;
import com.zyh.spring.take_away.service.OrderService;
import com.zyh.spring.take_away.util.ThreadLocalContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Api(tags = "订单控制层")
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation("用户订单提交")
    @RequestMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("订单提交成功");
    }
    @ApiOperation("获取用户的订单详情")
    @GetMapping("/userPage")
    public R<Page<Orders>> getUserOrder(@RequestParam("page")Long page,
                                        @RequestParam("pageSize") Long pageSize){

        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId,ThreadLocalContext.get());
        orderService.page(ordersPage,ordersLambdaQueryWrapper);
        return R.success(ordersPage);
    }
}
