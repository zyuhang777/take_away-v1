package com.zyh.spring.take_away.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.spring.take_away.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
