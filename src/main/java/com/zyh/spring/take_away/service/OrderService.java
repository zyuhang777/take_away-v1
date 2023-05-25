package com.zyh.spring.take_away.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.spring.take_away.pojo.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
