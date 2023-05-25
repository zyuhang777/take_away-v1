package com.zyh.spring.take_away.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.spring.take_away.commen.R;
import com.zyh.spring.take_away.pojo.Category;
import com.zyh.spring.take_away.pojo.Dish;
import com.zyh.spring.take_away.pojo.Setmeal;
import com.zyh.spring.take_away.service.CategoryService;
import com.zyh.spring.take_away.service.DishService;
import com.zyh.spring.take_away.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Api(tags = "菜品类别管理")
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    @ApiOperation("添菜品类别")
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("添加菜品类别");
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public R<Page<Category>> page(Long page, Long pageSize) {
        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(categoryPage, lambdaQueryWrapper);
        return R.success(categoryPage);
    }

    @ApiOperation("删除菜品类别")
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id) {

        boolean remove = categoryService.removeCategoryById(id);
        return remove ? R.success("删除成功") : R.error("删除失败或许时绑定了菜品或是套餐");

    }

    @ApiOperation("更新菜品类别")
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        boolean b = categoryService.updateById(category);
        return b ? R.success("修改成功") : R.error("修改失败");
    }
    @ApiOperation("获取菜品列表")
    @GetMapping("/list")
    public R<List<Category>> list(String type){
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(type!=null,Category::getType,type);
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(categoryList);
    }
//
}
