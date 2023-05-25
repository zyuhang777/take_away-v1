package com.zyh.spring.take_away.service.serviiceImp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.spring.take_away.dto.DishDto;
import com.zyh.spring.take_away.mapper.DishMapper;
import com.zyh.spring.take_away.pojo.Dish;
import com.zyh.spring.take_away.pojo.DishFlavor;
import com.zyh.spring.take_away.service.DishFlavorService;
import com.zyh.spring.take_away.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Service
public class DishServiceImp extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorService dishFlavorService;
    @Value("${img.path}")
    String path;

    @Override
    public Page<Dish> pageSelect(Page<Dish> dishPage, String name) {
        Page<Dish> page = dishMapper.pageSelect(dishPage, name);
        return page;
    }

    @Override
    @Transactional
    public void saveDishAndDishFlavor(DishDto dishDto) {
        save(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor dishFlavor : flavors) {
            dishFlavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    @Transactional
    public void updateDish(DishDto dish) {
        Long id = dish.getId();
        Dish srcDish = getById(id);
        String image = srcDish.getImage();
        image = path + image;
        File file = new File(image);
        updateById(dish);
        List<DishFlavor> flavors = dish.getFlavors();
        for (DishFlavor dishFlavor : flavors) {
            dishFlavor.setDishId(dish.getId());
        }

        dishFlavorService.saveOrUpdateBatch(flavors);
        if (file.exists()) {
            file.delete();
        }

    }

    @Override
    public DishDto getDishDto(String id) {
        Dish dish = getById(id);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        DishDto dishDto = new DishDto();
        if (dish!=null){
            BeanUtils.copyProperties(dish, dishDto);
        }
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }

    @Override
    @Transactional
    public void deleteDishDto(Long id) {
        String image = dishMapper.selectById(id).getImage();
        dishMapper.deleteById(id);
        dishFlavorService.remove(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId,id));
        image = path+image;
        File file = new File(image);
        if (file.exists()){
            file.delete();
        }
    }

    @Override
    public void updateStatus(String status, Long id) {
        dishMapper.updateStatus(status,id);
    }

}
