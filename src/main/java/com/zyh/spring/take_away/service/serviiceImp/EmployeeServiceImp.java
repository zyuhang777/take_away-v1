package com.zyh.spring.take_away.service.serviiceImp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.spring.take_away.mapper.EmployeeMapper;
import com.zyh.spring.take_away.pojo.Employee;
import com.zyh.spring.take_away.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Service
public class EmployeeServiceImp extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;
    @Override
    public Page<Employee> pageSelect(Page<Employee> employeePage, String name) {
        return baseMapper.selectPageByName(employeePage,name);
    }
}
