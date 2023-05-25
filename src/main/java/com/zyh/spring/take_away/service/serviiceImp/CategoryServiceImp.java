package com.zyh.spring.take_away.service.serviiceImp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.spring.take_away.mapper.CategoryMapper;
import com.zyh.spring.take_away.pojo.Category;
import com.zyh.spring.take_away.pojo.Dish;
import com.zyh.spring.take_away.pojo.Setmeal;
import com.zyh.spring.take_away.service.CategoryService;
import com.zyh.spring.take_away.service.DishService;
import com.zyh.spring.take_away.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Service
public class CategoryServiceImp extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    SetmealService setmealService;
    @Autowired
    DishService dishService;
    @Override
    public boolean removeCategoryById(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        List<Dish> dishList = dishService.list(dishLambdaQueryWrapper);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        List<Setmeal> setmealList = setmealService.list(setmealLambdaQueryWrapper);
        if (dishList.size()!=0||setmealList.size()!=0){
            return false;
        }else {
            removeById(id);
            return true;
        }

    }
}
