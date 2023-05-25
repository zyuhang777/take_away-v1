package com.zyh.spring.take_away.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.spring.take_away.dto.DishDto;
import com.zyh.spring.take_away.pojo.Dish;


public interface DishService extends IService<Dish> {
    Page<Dish> pageSelect(Page<Dish> dishPage, String name);

    void saveDishAndDishFlavor(DishDto dishDto);

    void updateDish(DishDto dish);

    DishDto getDishDto(String id);

    void deleteDishDto(Long id);

    void updateStatus(String status, Long id);
}
