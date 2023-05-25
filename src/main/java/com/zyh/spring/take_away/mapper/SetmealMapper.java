package com.zyh.spring.take_away.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.spring.take_away.pojo.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    void updateStatusById(@Param("status") String status, @Param("id") String id);
}
