package com.zyh.spring.take_away.service.serviiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.spring.take_away.mapper.ShoppingCartMapper;
import com.zyh.spring.take_away.pojo.ShoppingCart;
import com.zyh.spring.take_away.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Service
public class ShoppingCartServiceImp extends
        ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {
    @Override
    public ShoppingCart selectDishOrSetmeal(ShoppingCart shoppingCart) {
        ShoppingCart shoppingDb = baseMapper.selectDishOrSetmeal(shoppingCart);
        return shoppingDb;
    }
}
