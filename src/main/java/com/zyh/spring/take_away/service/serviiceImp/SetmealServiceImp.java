package com.zyh.spring.take_away.service.serviiceImp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.spring.take_away.commen.Exception.TakeAwayException;
import com.zyh.spring.take_away.commen.R;
import com.zyh.spring.take_away.dto.SetmealDto;
import com.zyh.spring.take_away.mapper.SetmealMapper;
import com.zyh.spring.take_away.pojo.Setmeal;
import com.zyh.spring.take_away.pojo.SetmealDish;
import com.zyh.spring.take_away.service.SetmealDishService;
import com.zyh.spring.take_away.service.SetmealService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.io.File;
import java.util.List;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Service
public class SetmealServiceImp extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Value("${img.path}")
    private String path;

    @Override
    @Transactional
    public void saveSetmealDto(SetmealDto setmealDto) {
        save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);

    }

    @Override
    @Transactional
    public void deleteByIds(String ids) {
        String[] idList = ids.split(",");
        for (String id : idList) {
            Setmeal setmeal = getById(id);
            if (setmeal.getStatus() == 0) {
                String image = setmeal.getImage();
                image = path + image;
                removeById(id);
                setmealDishService.remove(new LambdaQueryWrapper<SetmealDish>().
                        eq(SetmealDish::getSetmealId, id));
                File file = new File(image);
                if (file.exists()) {
                    file.delete();
                }
            } else {
                throw new TakeAwayException("套餐删除失败，改套餐的售卖状态为正在售卖");
            }
        }

    }

    @Override
    @Transactional
    public void updateSetmealDto(SetmealDto setmealDto) {
        String image = setmealDto.getImage();
        image = path + image;
        updateById(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setDishId(setmealDto.getId());
            setmealDishService.updateById(setmealDish);
        }
        File file = new File(image);
        if (file.exists()) {
            file.delete();
        }


    }

    @Override
    public void updateStatus(String status, String ids) {
        String[] idList = ids.split(",");
        for (String id :idList) {
            baseMapper.updateStatusById(status,id);
        }
    }

}
