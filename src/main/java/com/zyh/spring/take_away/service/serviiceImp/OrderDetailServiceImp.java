package com.zyh.spring.take_away.service.serviiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.spring.take_away.mapper.OrderDetailMapper;
import com.zyh.spring.take_away.pojo.OrderDetail;
import com.zyh.spring.take_away.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Service
public class OrderDetailServiceImp extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
