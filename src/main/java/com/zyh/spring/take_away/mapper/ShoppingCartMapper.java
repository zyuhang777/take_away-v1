package com.zyh.spring.take_away.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.spring.take_away.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    ShoppingCart selectDishOrSetmeal(@Param("shoppingCart") ShoppingCart shoppingCart);
}
