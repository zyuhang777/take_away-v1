package com.zyh.spring.take_away.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.spring.take_away.commen.R;
import com.zyh.spring.take_away.dto.DishDto;
import com.zyh.spring.take_away.pojo.Category;
import com.zyh.spring.take_away.pojo.Dish;
import com.zyh.spring.take_away.pojo.DishFlavor;
import com.zyh.spring.take_away.service.CategoryService;
import com.zyh.spring.take_away.service.DishFlavorService;
import com.zyh.spring.take_away.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Api(tags = "菜品管理")
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public R<Page<DishDto>> page(Long page, Long pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<Dish> dishPageResult = dishService.pageSelect(dishPage, name);
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPageResult, dishDtoPage, "records");
        List<Dish> dishes = dishPage.getRecords();
        ArrayList<DishDto> dishDtoList = new ArrayList<>();
        for (Dish dish : dishes) {
            Long id = dish.getCategoryId();
            LambdaQueryWrapper<Category> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishLambdaQueryWrapper.eq(Category::getId, id);
            Category category = categoryService.getOne(dishLambdaQueryWrapper);
            String categoryName = category.getName();
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setCategoryName(categoryName);
            dishDtoList.add(dishDto);
        }
        dishDtoPage.setRecords(dishDtoList);

        return R.success(dishDtoPage);
    }

    @ApiOperation("增加菜品")
    @PostMapping()
    public R<String> addDish(@RequestBody DishDto dishDto) {
        dishService.saveDishAndDishFlavor(dishDto);


        return R.success("添加成功");
    }

    @ApiOperation("通过id获取菜品信息")
    @GetMapping("{id}")
    public R<DishDto> getDishById(@PathVariable String id) {
        DishDto dishDto = dishService.getDishDto(id);
        return R.success(dishDto);
    }

    @ApiOperation("修改菜品信息")
    @PutMapping()
    public R<String> update(@RequestBody DishDto dish) {
        dishService.updateDish(dish);
        return R.success("修改成功");
    }

    @ApiOperation("通过id删除菜品")
    @DeleteMapping()
    public R<String> delete(@RequestParam("ids") Long id) {
        dishService.deleteDishDto(id);
        return R.success("删除成功");
    }

    @ApiOperation("通过id更改菜品的售卖状态")
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable String status, @RequestParam("ids") Long id) {
        dishService.updateStatus(status, id);
        return R.success("菜品售卖状态以更新");
    }

    /**
     *
     * @param dish 里面有id json格式的接收
     * @return
     */
    @ApiOperation("通过菜品分类获取菜品")
    @GetMapping("/list")
    /**
     * 刚开始是使用的是List<Dish> 后来考虑到 移动端选规格或是选（选口味） 使用了Dto格式 DishDto
     */
    public R<List<DishDto>> getDishByCategory( Dish dish) {
        List<DishDto> dishDtoArrayList = dishService.getListByCategoryId(dish);
        return R.success(dishDtoArrayList);
    }

}
