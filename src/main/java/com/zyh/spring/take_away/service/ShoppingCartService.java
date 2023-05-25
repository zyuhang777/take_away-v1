package com.zyh.spring.take_away.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.spring.take_away.pojo.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    ShoppingCart selectDishOrSetmeal(ShoppingCart shoppingCart);
}
