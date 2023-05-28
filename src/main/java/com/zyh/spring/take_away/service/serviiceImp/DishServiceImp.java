package com.zyh.spring.take_away.service.serviiceImp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.spring.take_away.commen.R;
import com.zyh.spring.take_away.dto.DishDto;
import com.zyh.spring.take_away.mapper.DishMapper;
import com.zyh.spring.take_away.pojo.Dish;
import com.zyh.spring.take_away.pojo.DishFlavor;
import com.zyh.spring.take_away.service.DishFlavorService;
import com.zyh.spring.take_away.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;
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
        Dish dish = new Dish();
        dish.setCategoryId(dishDto.getCategoryId());
        getListByCategoryId(dish,"1");


    }

    @Override
    @Transactional
    public void updateDish(DishDto dish) {
        Long id = dish.getId();
        Dish srcDish = getById(id);
        Long categoryId = srcDish.getCategoryId();
        Long categoryId1 = dish.getCategoryId();
        String key1 = "category_" + categoryId+"_1";
        String key2 = "category_" + categoryId1+"_1";
        List<String> keys = new ArrayList<>();
        keys.add(key1);
        keys.add(key2);
        redisTemplate.delete(key1);
        redisTemplate.delete(key2);
        updateById(dish);
        List<DishFlavor> flavors = dish.getFlavors();
        for (DishFlavor dishFlavor : flavors) {
            dishFlavor.setDishId(dish.getId());
        }

        dishFlavorService.saveOrUpdateBatch(flavors);


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
        Dish dish = dishMapper.selectById(id);
        String image = dish.getImage();
        dishMapper.deleteById(id);
        dishFlavorService.remove(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId,id));
        image = path+image;
        File file = new File(image);
        if (file.exists()){
            file.delete();
        }
        getListByCategoryId(dish,"1");
    }

    @Override
    public void updateStatus(String status, Long id) {
        dishMapper.updateStatus(status,id);
        Dish dish = dishMapper.selectById(id);
        getListByCategoryId(dish,"1");
    }

    @Override
    public List<DishDto> getListByCategoryId(Dish dish,String ... up) {
        List<DishDto> dishDtoArrayList = null;
        String key = "category_" + dish.getCategoryId() +"_" + dish.getStatus();
        dishDtoArrayList = (List<DishDto>)redisTemplate.opsForValue().get(key);
//         检查是不是第一次访问
        if (dishDtoArrayList!=null && up==null){
            return dishDtoArrayList;
        }

        Long id = dish.getCategoryId();
        LambdaQueryWrapper<Dish> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Dish::getCategoryId, id).eq(Dish::getStatus,1);
        categoryLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = list(categoryLambdaQueryWrapper);
        dishDtoArrayList = new ArrayList<>();
        for (Dish dish1 :dishList) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1,dishDto);
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish1.getId());
            List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(list);
            dishDtoArrayList.add(dishDto);
        }
        redisTemplate.opsForValue().set(key,dishDtoArrayList,60, TimeUnit.MINUTES);
        return dishDtoArrayList;
    }

}
