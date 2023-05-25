package com.zyh.spring.take_away.service.serviiceImp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.zyh.spring.take_away.commen.Exception.TakeAwayException;
import com.zyh.spring.take_away.mapper.OrderMapper;
import com.zyh.spring.take_away.pojo.AddressBook;
import com.zyh.spring.take_away.pojo.OrderDetail;
import com.zyh.spring.take_away.pojo.Orders;
import com.zyh.spring.take_away.pojo.ShoppingCart;
import com.zyh.spring.take_away.service.*;
import com.zyh.spring.take_away.util.ThreadLocalContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Service
public class OrderServiceImp extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void submit(Orders orders) {
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, ThreadLocalContext.get());
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        if (shoppingCarts==null){
            throw new TakeAwayException("购物车为空不能结算");
        }
        BigDecimal amountS = new BigDecimal(0);
        for (ShoppingCart shoppingCart : shoppingCarts) {
            Integer number = shoppingCart.getNumber();
            BigDecimal amount = shoppingCart.getAmount();
            amountS = new BigDecimal(number * amount.intValue() + amountS.intValue());
        }

        orders.setUserId(ThreadLocalContext.get());
        orders.setStatus(2);
        orders.setOrderTime(LocalDateTime.now());
        orders.setAmount(amountS);
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        String orderNumber = UUID.randomUUID().toString();
        orders.setNumber(orderNumber);
        orders.setCheckoutTime(LocalDateTime.now());
        baseMapper.insert(orders);
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCart :shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(userService.getById(ThreadLocalContext.get()).getName());
            orderDetail.setOrderId(orders.getId());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetails.add(orderDetail);
        }
        orderDetailService.saveBatch(orderDetails);
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper1.eq(ShoppingCart::getUserId,ThreadLocalContext.get());
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper1);


    }
}
