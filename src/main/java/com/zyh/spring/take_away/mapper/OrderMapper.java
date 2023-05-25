package com.zyh.spring.take_away.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.spring.take_away.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
