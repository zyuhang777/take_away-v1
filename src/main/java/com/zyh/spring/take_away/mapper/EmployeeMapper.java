package com.zyh.spring.take_away.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.spring.take_away.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    Page<Employee> selectPageByName(Page<Employee> employeePage, @Param("name") String name);
}
