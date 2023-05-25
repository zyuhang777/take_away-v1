package com.zyh.spring.take_away.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.spring.take_away.commen.R;
import com.zyh.spring.take_away.dto.SetmealDto;
import com.zyh.spring.take_away.pojo.Category;
import com.zyh.spring.take_away.pojo.Setmeal;
import com.zyh.spring.take_away.pojo.SetmealDish;
import com.zyh.spring.take_away.service.CategoryService;
import com.zyh.spring.take_away.service.SetmealDishService;
import com.zyh.spring.take_away.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Api(tags = "套餐管理")
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService  setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(@RequestParam("page") Long page,
                                    @RequestParam("pageSize") Long pageSize,
                                    @RequestParam(value = "name",required = false) String name){
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        setmealService.page(setmealPage,setmealLambdaQueryWrapper);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        ArrayList<SetmealDto> setmealDtos = new ArrayList<>();
        for (Setmeal setmeal :records) {
            Long categoryId = setmeal.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            setmealDto.setCategoryName(categoryName);
            setmealDtos.add(setmealDto);
        }
        setmealDtoPage.setRecords(setmealDtos);

        return R.success(setmealDtoPage);

    }
    @ApiOperation("添加套餐")
    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.saveSetmealDto(setmealDto);
        return R.success("添加成功");
    }
    @ApiOperation("根据id删除套餐")
    @DeleteMapping
    public R<String> delete(@Param("ids")String ids){
        setmealService.deleteByIds(ids);
        return R.success("删除套餐成功了");
    }
    @ApiOperation("通过id获取套餐信息")
    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealById(@PathVariable String id){
        Setmeal setmeal = setmealService.getById(id);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishLambdaQueryWrapper);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(setmealDishes);
        return R.success(setmealDto);

    }
    @ApiOperation("修改套餐信息")
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetmealDto(setmealDto);
        return R.success("修改套餐信息成功");
    }
    @ApiOperation("套餐售卖状态更新")
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable String status,@RequestParam("ids")String ids){
        setmealService.updateStatus(status,ids);
        return R.success("套餐售卖状态更新成功");
    }
    @ApiOperation("获取套餐列表")
    @GetMapping("/list")
    public R<List<Setmeal>> getSetmealLis(@RequestParam("categoryId")String categoryId,
                                          @RequestParam("status")String status){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId,categoryId).eq(Setmeal::getStatus,status);
        List<Setmeal> setmealList = setmealService.list(wrapper);
        return R.success(setmealList);

    }
}
