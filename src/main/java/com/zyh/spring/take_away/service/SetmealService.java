package com.zyh.spring.take_away.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyh.spring.take_away.dto.SetmealDto;
import com.zyh.spring.take_away.pojo.Setmeal;
import org.apache.ibatis.annotations.Mapper;


public interface SetmealService extends IService<Setmeal> {
    void saveSetmealDto(SetmealDto setmealDto);

    void deleteByIds(String ids);

    void updateSetmealDto(SetmealDto setmealDto);

    void updateStatus(String status, String ids);
}
